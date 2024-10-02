package basico.task.management.model.plataforma;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "promociones_prx")
@Data
public class PrinexPromotion implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codpromo", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "sppromd")
	private String sppromd;
	
	@Column(name = "spsocic")
	private Long spsocic;
	
	@Column(name = "spsocid")
	private String spsocid;
	
	@Column(name = "uuusua")
	private String comercial;


}
