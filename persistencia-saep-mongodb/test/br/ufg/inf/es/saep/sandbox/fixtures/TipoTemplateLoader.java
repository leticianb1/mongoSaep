package br.ufg.inf.es.saep.sandbox.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

public class TipoTemplateLoader implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Tipo.class).addTemplate("tipoDocente", new Rule() {
			{
				add("id", "primeiro");
				add("nome", "docente");
				add("descricao", "Tipo de atributo docente");
				add("atributos", has(2).of(Atributo.class, "atributoNome", "atributoCargaHoraria"));
			}
		});
	}

}
