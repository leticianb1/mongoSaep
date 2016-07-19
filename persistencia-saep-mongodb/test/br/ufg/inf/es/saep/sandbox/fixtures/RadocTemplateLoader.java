package br.ufg.inf.es.saep.sandbox.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;

public class RadocTemplateLoader implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Radoc.class).addTemplate("radocFixo", new Rule() {{
			add("anoBase", 2015);
			add("id", "radocFixo");
			add("relatos", has(2).of(Relato.class, "relatoDocente", "relatoTeste"));
		}});
		Fixture.of(Radoc.class).addTemplate("radocAleatorio", new Rule() {{
			add("anoBase", 2015);
			add("id", random(Long.class, range(1L, 200L)).toString());
			add("relatos", has(2).of(Relato.class, "relatoDocente", "relatoTeste"));
		}});
	}

}
