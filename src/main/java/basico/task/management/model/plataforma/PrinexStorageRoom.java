package basico.task.management.model.plataforma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "trasteros_prx")
@Data
public class PrinexStorageRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_promoactivo", unique = true, nullable = false)
	private String id;
	
	@Column(name = "codpromo")
	private Long promotionId;
	
	@Column(name = "sppromd")
	private String promotion;
	
	@Column(name = "spsocid")
	private Long societyId;
	
	@Column(name = "spsocic")
	private String society;
	
	@Column(name = "descripcion")
	private String description;
	
	@Column(name = "rent")
	private Float rent;


	
}
