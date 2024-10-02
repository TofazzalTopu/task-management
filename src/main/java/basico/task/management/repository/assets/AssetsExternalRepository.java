package basico.task.management.repository.assets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import basico.task.management.projection.AssetsExternalResponse;
import basico.task.management.model.assets.AssetsExternal;


@Repository
public interface AssetsExternalRepository extends JpaRepository<AssetsExternal,Long>{

	 /**
     * @return this query will return the assets details from fuhrent which is found at basico_pbi.prx_fuhrent T1
     */
	@Query(value = "SELECT URALIA AS location,URCODPOSTAL AS province,URPROM AS poromottionName\n" +
			"        ,URSOCI AS socityId,UREDIT AS assetId,URPOBLACION AS direction,\n" +
			"       case\n" +
			"           when  FECMODIF > sysdate then 'Squatted'\n" +
			"           else\n" +
			"               'Precarious'\n" +
			"           end as type\n" +
			"FROM CL3349.fuhrent where ROWNUM<100",nativeQuery = true)
	List<AssetsExternalResponse>  findAllAssets();


	@Query(value = "SELECT distinct\n" +
			"    imp.aim_id as id,\n" +
			"    rr.arr_activo_descripcion as description,\n" +
			"    rr.arr_activo_tipo_prx as arrType,\n" +
			"    rr.arr_activo as code,\n" +
			"    imp.aim_sociedad as societyId,\n" +
			"    imp.aim_promocion as promotionId,\n" +
			"    rr.arr_sociedad_literal as societyName,\n" +
			"    rr.arr_promocion_literal as promotionName,\n" +
			"    rr.arr_comunidad as community,\n" +
			"    rr.arr_provincia as province,\n" +
			"    rr.arr_municipio as municipality\n" +
			"from alq_rentroll_est rr\n" +
			"    left join alq_impagados imp\n" +
			"on imp. aim_promocion = rr. arr_promocion\n" +
			"    and imp. aim_operacion = rr. arr_operacion\n" +
			"    and imp. aim_activo = rr.arr_activo\n" +
			"where imp.aim_situacion in (19,99)\n" +
			"and     rr.arr_estado=25\n" +
			"     AND rr.arr_situacion=20\n" +
			"     AND rr.arr_activo_estado_literal IN\n" +
			"('06. PRECARIOS', '06. PRECARIOS LT', '06. PRECARIOS ST')\n" +
			"     AND rr.arr_operacion_fecha_fin<NOW()\n" +
			"group by imp.aim_situacion,imp.aim_activo\n" +
			"order by imp.aim_situacion,imp.aim_activo;",nativeQuery = true)
	List<AssetsExternalResponse> findAllPrecarious();


	@Query(value = "SELECT distinct\n" +
			"    imp.aim_id as id,\n" +
			"    rr.arr_activo_descripcion as description,\n" +
			"    rr.arr_activo_tipo_prx as arrType,\n" +
			"    rr.arr_activo as code,\n" +
			"    imp.aim_sociedad as societyId,\n" +
			"    imp.aim_promocion as promotionId,\n" +
			"    rr.arr_sociedad_literal as societyName,\n" +
			"    rr.arr_promocion_literal as promotionName,\n" +
			"    rr.arr_comunidad as community,\n" +
			"    rr.arr_provincia as province,\n" +
			"    rr.arr_municipio as municipality\n" +
			"from alq_rentroll_est rr\n" +
			"    left join alq_impagados imp\n" +
			"on imp. aim_promocion = rr. arr_promocion\n" +
			"    and imp. aim_operacion = rr. arr_operacion\n" +
			"    and imp. aim_activo = rr.arr_activo\n" +
			"where imp.aim_situacion in (19,99)\n" +
			"and  rr.arr_estado=25\n" +
			"  AND rr.arr_situacion=30\n" +
			"  AND  rr.arr_activo_estado_literal IN ('07. SQUATTER')\n" +
			"group by imp.aim_situacion,imp.aim_activo\n" +
			"order by imp.aim_situacion,imp.aim_activo;",nativeQuery = true)
	List<AssetsExternalResponse> findAllSquatted();


}
