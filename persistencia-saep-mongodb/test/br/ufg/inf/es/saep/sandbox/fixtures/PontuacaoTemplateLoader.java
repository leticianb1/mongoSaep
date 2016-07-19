package br.ufg.inf.es.saep.sandbox.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

public class PontuacaoTemplateLoader implements TemplateLoader{

	@Override
	public void load() {
		Fixture.of(Pontuacao.class).addTemplate("pontuacaoAleatoria", new Rule() {{
			add("atributo", uniqueRandom("nome", "cargaHoraria", "trabalho", "paginas") );
			add("valor", new Valor(random(Long.class, range(1L, 20L)).toString()));
		}});
		
		Fixture.of(Pontuacao.class).addTemplate("pontuacaoNota", new Rule() {{
			add("atributo", uniqueRandom("trabalho") );
			add("valor", new Valor(random(Long.class, range(1L, 20L)).toString()));
		}});
		
		Fixture.of(Pontuacao.class).addTemplate("pontuacaoNotaFixa", new Rule() {{
			add("atributo", uniqueRandom("trabalho") );
			add("valor", new Valor(30));
		}});
	}

}
