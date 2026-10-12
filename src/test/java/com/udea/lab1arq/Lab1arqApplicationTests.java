package com.udea.lab1arq;

import com.udea.lab1arq.DTO.CustomerDTO;
import com.udea.lab1arq.entity.Customer;
import com.udea.lab1arq.mapper.CustomerMapper;
import com.udea.lab1arq.repository.CustomerRepository;
import com.udea.lab1arq.repository.TransactionRepository;
import com.udea.lab1arq.service.CustomerService;
import com.udea.lab1arq.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la capa de servicio.
 * No levanta contexto Spring → más rápidas, 0 dependencia de DB.
 * JaCoCo las instrumenta y genera cobertura para SonarCloud.
 */
@ExtendWith(MockitoExtension.class)
class Lab1arqApplicationTests {

	// ── CustomerService ──────────────────────────────────────────
	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private CustomerMapper customerMapper;

	@InjectMocks
	private CustomerService customerService;

	// ── TransactionService ───────────────────────────────────────
	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	private Customer sampleCustomer;
	private CustomerDTO sampleCustomerDTO;

	@BeforeEach
	void setUp() {
		sampleCustomer = new Customer();
		sampleCustomer.setId(1L);
		sampleCustomer.setFirstName("Juan");
		sampleCustomer.setLastName("Pérez");
		sampleCustomer.setAccountNumber("ACC-001");
		sampleCustomer.setBalance(1000.0);

		// CustomerDTO solo tiene constructor con parámetros
		sampleCustomerDTO = new CustomerDTO(1L, "Juan", "Pérez", "ACC-001", 1000.0);
	}

	/** getAllCustomer devuelve lista vacía cuando el repositorio no tiene datos */
	@Test
	void getAllCustomers_whenEmpty_returnsEmptyList() {
		when(customerRepository.findAll()).thenReturn(Collections.emptyList());

		List<CustomerDTO> result = customerService.getAllCustomer();

		assertThat(result).isEmpty();
		verify(customerRepository, times(1)).findAll();
	}

	/** getAllCustomer mapea y devuelve los clientes correctamente */
	@Test
	void getAllCustomers_returnsMappedList() {
		when(customerRepository.findAll()).thenReturn(List.of(sampleCustomer));
		when(customerMapper.toDTO(sampleCustomer)).thenReturn(sampleCustomerDTO);

		List<CustomerDTO> result = customerService.getAllCustomer();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getFirstName()).isEqualTo("Juan");
	}

	/** getCustomerById lanza excepción cuando el cliente no existe */
	@Test
	void getCustomerById_notFound_throwsRuntimeException() {
		when(customerRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.getCustomerById(99L))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Customer not found");
	}

	/** getCustomerById devuelve el DTO cuando el cliente existe */
	@Test
	void getCustomerById_found_returnsDTO() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(sampleCustomer));
		when(customerMapper.toDTO(sampleCustomer)).thenReturn(sampleCustomerDTO);

		CustomerDTO result = customerService.getCustomerById(1L);

		assertThat(result.getAccountNumber()).isEqualTo("ACC-001");
	}

	/** createCustomer persiste y devuelve el DTO correcto */
	@Test
	void createCustomer_savesAndReturnsDTO() {
		when(customerMapper.toEntity(sampleCustomerDTO)).thenReturn(sampleCustomer);
		when(customerRepository.save(sampleCustomer)).thenReturn(sampleCustomer);
		when(customerMapper.toDTO(sampleCustomer)).thenReturn(sampleCustomerDTO);

		CustomerDTO result = customerService.createCustomer(sampleCustomerDTO);

		assertThat(result.getLastName()).isEqualTo("Pérez");
		verify(customerRepository, times(1)).save(sampleCustomer);
	}

	/** deleteCustomer lanza excepción si el cliente no existe */
	@Test
	void deleteCustomer_notFound_throwsRuntimeException() {
		when(customerRepository.existsById(99L)).thenReturn(false);

		assertThatThrownBy(() -> customerService.deleteCustomer(99L))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Customer not found");
	}

	/** deleteCustomer llama a deleteById cuando el cliente existe */
	@Test
	void deleteCustomer_exists_callsDeleteById() {
		when(customerRepository.existsById(1L)).thenReturn(true);

		customerService.deleteCustomer(1L);

		verify(customerRepository, times(1)).deleteById(1L);
	}

	/** getTransactionsForAccount devuelve lista vacía cuando no hay transacciones */
	@Test
	void getTransactionsForAccount_noTransactions_returnsEmpty() {
		when(transactionRepository
				.findBySenderAccountNumberOrReceiverAccountNumber("ACC-X", "ACC-X"))
				.thenReturn(Collections.emptyList());

		List<?> result = transactionService.getTransactionsForAccount("ACC-X");

		assertThat(result).isEmpty();
	}
}
