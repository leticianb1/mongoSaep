package br.ufg.inf.es.saep.sandbox.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.ufg.inf.es.saep.sandbox.dominio.Atributo;

public class AtributoTemplateLoader implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Atributo.class).addTemplate("atributoNome", new Rule() {{
			add("nome", "nome");
			add("descricao", "Nome do Docente");
			add("tipo", 2);
		}});
		
		Fixture.of(Atributo.class).addTemplate("atributoCargaHoraria", new Rule() {{
			add("nome", "cargaHoraria");
			add("descricao", "Carga horaria do docente");
			add("tipo", 2);
		}});

		Fixture.of(Atributo.class).addTemplate("atributoTrabalho", new Rule() {{
			add("nome", "trabalho");
			add("descricao", "Titulo do trabalho");
			add("tipo", 2);
		}});
		
		Fixture.of(Atributo.class).addTemplate("atributoPagina", new Rule() {{
			add("nome", "paginas");
			add("descricao", "Quantidade de paginas do trabalho");
			add("tipo", 2);
		}});
	}
}
