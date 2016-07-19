package br.ufg.inf.es.saep.sandbox.fixtures;

import java.util.HashMap;
import java.util.Map;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

public class RelatoTemplateLoader implements TemplateLoader {

	@Override
	public void load() {
		Map<String, Valor> valores = new HashMap<String, Valor>();
		valores.put("nome", new Valor("Fulano da Silva"));
		valores.put("cargaHoraria", new Valor(32));
		
		Map<String, Valor> valoresTeste = new HashMap<String, Valor>();
		valores.put("trabalho", new Valor("Pesquisa em Teste de Software"));
		valores.put("paginas", new Valor(40));

		Fixture.of(Relato.class).addTemplate("relatoDocente", new Rule() {{
			add("tipo", "primeiro");
			add("valores", valores);
		}});
		
		Fixture.of(Relato.class).addTemplate("relatoTeste", new Rule() {{
			add("tipo", "segundo");
			add("valores", valoresTeste);
		}});
	}

}