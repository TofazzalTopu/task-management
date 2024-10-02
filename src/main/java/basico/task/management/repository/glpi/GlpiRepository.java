package basico.task.management.repository.glpi;

import basico.task.management.projection.GlpiFileResponse;
import basico.task.management.projection.GlpiResponse;
import basico.task.management.model.glpi.GlpiExternalTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GlpiRepository extends JpaRepository<GlpiExternalTickets,Long> {


    /**
     * @return this query will return the GLPI task details based on the category INCIDENCIAS TECNICAS which we can get from category of glpi_category

    @Query(value = "SELECT t.name,t.content,t.date_creation as datecreatation,t.status,t.id,t.itilcategories_id as itilcategoriesid,t.closedate,t.solvedate,c.name as category\n" +
            "FROM glpi_tickets t,glpi_tickets_users tu,glpi_itilcategories c\n" +
            "WHERE t.id = tu.tickets_id\n" +
            "  AND t.itilcategories_id = c.id\n" +
            "  AND t.is_deleted = 0\n" +
            "  AND t.date_creation >= '2021-04-01 00:00:00'\n" +
            "  AND c.completename='CONTACT-CENTER > INCIDENCIAS TECNICAS'",nativeQuery = true)
     */

    @Query(value ="select distinct  t.content as description,t.id as ticketId,s.completename as completeName,t.name as ticketName,s.building as code, s.state as province, s.address as direction,s.town as city,concat(ur.name,'@basico.es') as email,gg.name as userGroup from glpi_tickets t\n" +
            "                        left join glpi_tickets_users u on t.id=u.tickets_id\n" +
            "                        left join glpi_users ur on u.users_id=ur.id\n" +
            "                        inner join glpi_groups_users g on ur.id=g.users_id\n" +
            "                        left join glpi_groups gg on g.groups_id = gg.groups_id\n" +
            "                        left join glpi_itilcategories c on t.itilcategories_id=c.id\n" +
            "                        left join glpi_locations s on t.locations_id=s.id\n" +
            "                        where\n" +
            "                        ur.name REGEXP  '^[A-z]+$'and t.locations_id not in (0)", nativeQuery = true)
    List<GlpiResponse>  findAllTickets();

    @Query(value = "select id,filename as fileName,mime,filepath as filePath from glpi_documents where tickets_id=?1",nativeQuery = true)
    List<GlpiFileResponse> findAllGlpiFile(Long ticketId);
}
