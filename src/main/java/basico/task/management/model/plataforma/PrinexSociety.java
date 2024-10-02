package basico.task.management.model.plataforma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "sociedades_prx1")
@Data
public class PrinexSociety {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "spsocic", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "spsocid")
	private String literalSociedad;
	
	@Column(name = "direccion")
	private String direccion;
	
	@Column(name = "iban")
	private String iban;
	
	@Column(name = "telefono")
	private String telefono;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "cif")
	private String cif;
	

}
