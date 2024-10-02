package basico.task.management.config.security;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Properties;

public class Ldap {
	
	private static InitialDirContext getContext() throws NamingException {
		Properties props = new Properties();
	    props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	    props.put(Context.SECURITY_AUTHENTICATION, "simple");
	    props.put(Context.PROVIDER_URL, "ldap://ldap.basico.es:389");
	    props.put(Context.SECURITY_PRINCIPAL, "CN=usrldap,CN=Users,DC=basico,DC=es");//adminuser - User with special priviledge, dn user
	    props.put(Context.SECURITY_CREDENTIALS, "Basico3349");//dn user password
	
	
	    return new InitialDirContext(props);
	}
	
	public static ResponseEntity<Object> Ldap(String username, String password) throws Exception{	
	    InitialDirContext context = getContext();
	
	    SearchControls ctrls = new SearchControls();
	    ctrls.setReturningAttributes(new String[] { "givenName", "sn","memberOf" });
	    ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    
	    NamingEnumeration<SearchResult> answers = context.search("CN=Users,DC=basico,DC=es", "(sAMAccountName=" + username + ")", ctrls);
	    if(answers.hasMore()) {
		    SearchResult result = answers.nextElement();
		
		    String user = result.getNameInNamespace();
		        
		    try {
		    	Properties props = new Properties();
		        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		        props.put(Context.PROVIDER_URL, "ldap://ldap.basico.es:389");
		        props.put(Context.SECURITY_PRINCIPAL, user);
		        props.put(Context.SECURITY_CREDENTIALS, password);
		
		   context = new InitialDirContext(props);
		   Attribute memberOf = result.getAttributes().get("memberOf");
		    NamingEnumeration<String> resultGroup = (NamingEnumeration<String>) memberOf.getAll();
		    ResponseEntity<Object> resultLdap = new ResponseEntity<Object>("{\"rol\":\"Comercial\",\"user\":\""+username+"\",",HttpStatus.OK);
		    while (resultGroup.hasMore()) {
		        String rol = resultGroup.next();
		        rol = rol.substring(3, rol.indexOf(','));
		        SearchControls ctrlsGroup = new SearchControls();
		        ctrlsGroup.setReturningAttributes(new String[] { "givenName", "uSNChanged" });
		        ctrlsGroup.setSearchScope(SearchControls.SUBTREE_SCOPE);
			    
			    NamingEnumeration<SearchResult> answersGroup = context.search("OU=Grupos,DC=basico,DC=es", "(cn=" + rol + ")", ctrlsGroup);
			    SearchResult resultGroupLDAP = answersGroup.nextElement();
			
			    Attribute group = resultGroupLDAP.getAttributes().get("uSNChanged");
			    String IDGroup = group.toString().substring(12);
		        if(rol.equals("Administrador"))
		        	return new ResponseEntity<Object>("{\"rol\":\"Administrador\",\"user\":\""+username+"\",",HttpStatus.OK);
		        else if(rol.equals("Tecnologia"))
		        	resultLdap = new ResponseEntity<Object>("{\"rol\":\"Administrador\",\"user\":\""+username+"\",",HttpStatus.OK);
		        else if(rol.equals("Admin WEB"))
		        	resultLdap = new ResponseEntity<Object>("{\"rol\":\"Administrador\",\"user\":\""+username+"\",",HttpStatus.OK);
		        else if(rol.equals("Administracion Carteras"))
		        	return new ResponseEntity<Object>("{\"rol\":\"Administracion\",\"user\":\""+username+"\",",HttpStatus.OK);
		        else if(rol.equals("Coordinadores"))
		        	return new ResponseEntity<Object>("{\"rol\":\"Coordinadores\",\"user\":\""+username+"\",",HttpStatus.OK);
		        else if(rol.equals("Aseguradora"))
		        	return new ResponseEntity<Object>("{\"rol\":\"Aseguradora\",\"user\":\""+username+"\",",HttpStatus.OK);
		     }
		    return resultLdap;
		    //return new ResponseEntity<Object>("Comercial",HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<Object>(e.getMessage(),HttpStatus.UNAUTHORIZED);
		    }	    
	    }else {
	    	return new ResponseEntity<Object>("Usuario no encontrado",HttpStatus.NOT_FOUND);
	    }
	}
	
	public static String getName(String username) throws Exception{
		
		InitialDirContext context = getContext();
	
	    SearchControls ctrls = new SearchControls();
	    ctrls.setReturningAttributes(new String[] { "cn", "sn","memberOf" });
	    ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    
	    NamingEnumeration<SearchResult> answers = context.search("CN=Users,DC=basico,DC=es", "(sAMAccountName=" + username + ")", ctrls);
	    SearchResult result = answers.nextElement();

	    return  result.getAttributes().get("cn").get(0).toString();
	    	    
	}
	
	public static String getComercials() throws Exception{
	    
	    return "["+getMembers(getContext(),"COMERCIAL")+"]";
	    	    
	}

	private static String getMembers(InitialDirContext context, String group) throws Exception{

	    SearchControls ctrls = new SearchControls();
	    ctrls.setReturningAttributes(new String[] { "cn", "sn","member" });
	    ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    
	    NamingEnumeration<SearchResult> answers = context.search("OU=Grupos,DC=basico,DC=es", "(cn="+group+")", ctrls);
	    SearchResult result = answers.nextElement();
	    String members = "";
	    Attribute attribute = result.getAttributes().get("member");
	    if(attribute!=null) {
	    	NamingEnumeration<?> resultMember = attribute.getAll();
		    while(resultMember.hasMoreElements()) {
		    	String member= resultMember.nextElement().toString();
		    	if(member.contains("CN=Users")) {
		    		if(members.equals("")) {
		    			members= getUser(context,member.substring(3,member.indexOf(",")));
		    		}else {
		    			members+= ","+getUser(context,member.substring(3,member.indexOf(",")));
		    		}
		    		
		    	}else if(member.contains("OU=Grupos")) {
		    		/*String resultMembers = getMembers(context,member.substring(3,member.indexOf(",")));
	    			if(members.equals("")) {
		    			members= resultMembers;
		    		}else {
		    			members+= ","+resultMembers;
		    		}*/		    			    		
		    	}
		    }
	    }	    
	    
	    return  members;    	    
	}
	
	private static String getUser(InitialDirContext context, String name) throws Exception{
		
	    SearchControls ctrls = new SearchControls();
	    ctrls.setReturningAttributes(new String[] { "cn", "sn","sAMAccountName" });
	    ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    
	    NamingEnumeration<SearchResult> answers = context.search("CN=Users,DC=basico,DC=es", "(cn="+name+")", ctrls);
	    SearchResult result = answers.nextElement();
	    return "{\"name\":\""+name+"\",\"user\":\""+result.getAttributes().get("sAMAccountName").get(0).toString()+"\"}";  	    
	}
}
