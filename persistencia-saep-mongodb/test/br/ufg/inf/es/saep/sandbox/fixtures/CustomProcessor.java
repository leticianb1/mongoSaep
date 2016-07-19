package br.ufg.inf.es.saep.sandbox.fixtures;


import br.com.six2six.fixturefactory.processor.Processor;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.es.saep.sandbox.mongo.ParecerRepositoryMongo;
import br.ufg.inf.es.saep.sandbox.mongo.ResolucaoRepositoryMongo;

public class CustomProcessor implements Processor {
    public void execute(Object object) {
    	
    	ParecerRepositoryMongo  parecerDao = new ParecerRepositoryMongo();
    	ResolucaoRepositoryMongo	resolucaoDao = new ResolucaoRepositoryMongo();
    		
       if(object instanceof Parecer){
    	   Parecer parecer = (Parecer) object;
    	   if(parecerDao.byId(parecer.getId()) != null){
    		   parecerDao.persisteParecer(parecer);
    	   }
       } else if (object instanceof Radoc) {
    	   Radoc radoc = (Radoc) object;
    	   if(parecerDao.radocById(radoc.getId()) != null){
    		   parecerDao.persisteRadoc(radoc);
    	   }
       } else if (object instanceof Tipo) {
    	 Tipo tipo = (Tipo) object;
    	 if(resolucaoDao.tipoPeloCodigo(tipo.getId()) != null){
    		 resolucaoDao.persisteTipo(tipo);
    	 }
       } else if (object instanceof Resolucao) {
    	   Resolucao resolucao = (Resolucao) object; 
    	   if(resolucaoDao.tipoPeloCodigo(resolucao.getId()) != null){
    	   resolucaoDao.persiste(resolucao);
    	   }
       }
    }
}
