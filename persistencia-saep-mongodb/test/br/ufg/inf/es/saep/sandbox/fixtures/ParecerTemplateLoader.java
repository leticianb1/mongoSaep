package br.ufg.inf.es.saep.sandbox.fixtures;

import java.util.ArrayList;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

public class ParecerTemplateLoader implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Tipo.class).addTemplate("parecer", new Rule() {{
			add("id", random(Long.class, range(1L, 200L)).toString());
			add("resolucao", "resolucao2013");
			add("radocs", new ArrayList<String>().add("01"));
			add("radocs", has(4).of(Radoc.class, "radocAleatorio" ));
			add("pontuacoes", has(4).of(Pontuacao.class, "pontuacaoAleatoria"));
			add("fundamentacao", "ficou �timo");
			add("notas", one(Nota.class, "notaPont"));
		}});
		Fixture.of(Tipo.class).addTemplate("parecerComRadocFixo", new Rule() {{
			add("id", random(Long.class, range(1L, 200L)).toString());
			add("resolucao", "resolucao2013");
			add("radocs", new ArrayList<String>().add("radocFixo"));
			add("pontuacoes", has(4).of(Pontuacao.class, "pontuacaoAleatoria"));
			add("fundamentacao", "ficou �timo");
			add("notas", one(Nota.class, "notaPont"));
		}});
		
	}

}
