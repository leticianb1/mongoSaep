package br.ufg.inf.es.saep.sandbox.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;

public class NotaTemplateLoader implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Pontuacao.class).addTemplate("notaPont", new Rule() {{
			add("original", one(Pontuacao.class, "pontuacaoNota") );
			add("novo", one(Pontuacao.class, "pontuacaoNota"));
			add("justificativa", "mudei pq eu quis");
		}});
		
		Fixture.of(Pontuacao.class).addTemplate("notaOriginal", new Rule() {{
			add("original", one(Pontuacao.class, "pontuacaoNotaFixa") );
			add("novo", one(Pontuacao.class, "pontuacaoNota"));
			add("justificativa", "mudei pq eu quis");
		}});
	}

}
