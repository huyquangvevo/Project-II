package com.huy.APIClient;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;

import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;
import com.franz.agraph.repository.AGTupleQuery;

public class ClientAPI {
	static private final String SERVER_URL = "http://localhost:10035";
	static private final String CATALOG_ID = "/";
	static private final String REPOSITORY_ID = "DBpedia";
	static private final String USERNAME = "quanghuy";
	static private final String PASSWORD = "49513801";
	static private final String TEMPORARY_DIRECTORY = "";
	private static List<AGRepositoryConnection> toClose = new ArrayList<AGRepositoryConnection>();
	public static AGRepositoryConnection connectionWine;
	static String resultAllInstance = "";
	
	public static AGRepositoryConnection connectToRepository(boolean close) throws Exception {
		println("\n Starting connection");
		AGServer server = new AGServer(SERVER_URL,USERNAME,PASSWORD);
		println("Available catalogs: "+server.listCatalogs());
		AGCatalog catalog = server.getCatalog(CATALOG_ID);
		println("Available repositries in catalog "+catalog.getCatalogName()+": "+catalog.listRepositories());
		
		closeAll();
		
		AGRepository wineOntology = catalog.openRepository(REPOSITORY_ID);
		wineOntology.initialize();
		AGRepositoryConnection conWine = wineOntology.getConnection();
		closeBeforeExit(conWine);
		println("Got a connection");
		println("Repository "+wineOntology.getRepositoryID()+" is up! It contains statements");
		connectionWine = conWine;
		return conWine;
		
	}
	
	//connectionWine = connectToRepository(false);
	
	public static String getInstanceOfClass(String nameClass) throws Exception {
		String res = "";
		try {
			String queryString = "SELECT ?s WHERE { ?s rdf:type " + nameClass + " . }";
			AGTupleQuery tupleQuery = connectionWine.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			TupleQueryResult result = tupleQuery.evaluate();
			try {
				while (result.hasNext()) {
					BindingSet bindingSet = result.next();
					Value s = bindingSet.getValue("s");
					String ss = s.toString();
					//System.out.format("%s \n",cutURL(ss));
					//res += cutURL(ss) + "\n";
					res += ss+"\n";
				}
			} finally {
				result.close();
			}
		//	count = tupleQuery.count();
		} finally {
			connectionWine.close();
		}
		return res;
	}
	
	
	
	public static String getPropertyOfInstance(String nameInstance) throws Exception {
		String resultHTML = ""; 
		try {
			String queryString = "SELECT ?s ?p ?o WHERE { " + nameInstance + " ?p ?o . }";
			AGTupleQuery tupleQuery = connectionWine.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			TupleQueryResult result = tupleQuery.evaluate();
			try {
				while (result.hasNext()) {
					BindingSet bindingSet = result.next();
					Value s = bindingSet.getValue("s");
					Value p = bindingSet.getValue("p");
					Value o = bindingSet.getValue("o");
					String ss;
					if(s==null)
						ss = nameInstance;
					else
						ss = s.toString();
					//System.out.format("%s %s %s \n",cutURL(ss),cutURL(p.toString()),cutURL(o.toString()));
				 	//resultHTML += String.format("%-50s %-50s %-50s \n", cutURL(ss),cutURL(p.toString()),cutURL(o.toString()));
					//resultHTML += cutURL(ss) + "   " + cutURL(p.toString()) + "   " + cutURL(o.toString()) + "\n";
				 	resultHTML += String.format("%-60s %-60s %-60s \n", ss,p.toString(),o.toString());

				}
			} finally {
				result.close();
			}
		} finally {
			connectionWine.close();
		}
		return resultHTML;
	}
	
	
	
	public static String getInstance(String nameClass,boolean flag) throws Exception {
		
		if(flag) {
			getInstance(nameClass,false); 
		} else {
			
			try {
				boolean hasSubclass;
				String queryString_test = "SELECT ?s WHERE { ?s rdfs:subClassOf " + nameClass + ". } ";
				AGTupleQuery tupleQuery_test = connectionWine.prepareTupleQuery(QueryLanguage.SPARQL, queryString_test);
				TupleQueryResult result_test = tupleQuery_test.evaluate();
				if(result_test.hasNext())
					hasSubclass = true;
				else 
					hasSubclass = false;
				
				if (hasSubclass) {
					while (result_test.hasNext()) {
						BindingSet bindingSet = result_test.next();
						Value s = bindingSet.getValue("s");
						String ss = s.toString();
						if(s.toString().indexOf("http")!=-1)
							ss = "<"+s.toString()+">";
						getInstance(ss,false);
					}
				} else {
					String queryString_ = "SELECT ?s WHERE { ?s rdf:type "+ nameClass + " . } ";
					AGTupleQuery tupleQuery = connectionWine.prepareTupleQuery(QueryLanguage.SPARQL, queryString_);
					TupleQueryResult result = tupleQuery.evaluate();
					try {
						while (result.hasNext()) {
							BindingSet bindingSet = result.next();
							Value s = bindingSet.getValue("s");
							String s_instance = s.toString();
						/*	if (s_instance.indexOf("#")!=-1)
								s_instance = s_instance.split("#")[1];
							System.out.format("%s \n", s_instance);
							*/
							//resultAllInstance += cutURL(s_instance)+"\n";
							resultAllInstance = s_instance + "\n";
						}
					} finally {
						result.close();
					}
				}
			} finally {
				connectionWine.close();
			}
			
		}
		
		return resultAllInstance;
		
	}
	
	
	public static String cutURL(String string) {
		
		if(string.indexOf("#") != -1) {
			string = string.split("#")[1];
			if(string.indexOf(">") == string.length()-1){
				string = string.substring(0, string.length()-1);
			}
		} else {
			string = string.substring(string.lastIndexOf("/")+1,string.length());
			if(string.indexOf(">") == string.length()-1)
				string = string.substring(0,string.length()-1);
		}
		
		return string;
		
	}
	
	public static void println(Object x) {
		System.out.println(x);
	}
	
	
	protected static void closeBeforeExit(AGRepositoryConnection conn) {
		
		toClose.add(conn);
		
	}
	
	
	static void close(AGRepositoryConnection conn) {
		try {
			conn.close();
		} catch (Exception e) {
			System.err.println("Erro closing repository connection: "+e);
			e.printStackTrace();
		}
	}
	
	
	
	protected static void closeAll() {
		while(!toClose.isEmpty()) {
			AGRepositoryConnection conn = toClose.get(0);
			close(conn);
			while (toClose.remove(conn)) {
				
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		
		connectToRepository(false);
		getInstanceOfClass("owl:Class");
		getPropertyOfInstance("<http://dbpedia.org/ontology/Spacecraft/dryCargo>");
		getInstance("owl:Class",true);
		//System.out.println(resultAllInstance+"ok");
		
	}
	
}
