package br.ufg.inf.es.saep.sandbox.fixtures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

public class TemplateLoader {

	public static void loadTemplates() {
		
		TemplateLoader.RelatoTemplate.loadTemplates();
		TemplateLoader.RadocTemplate.loadTemplates();
		TemplateLoader.TipoTemplate.loadTemplates();
		TemplateLoader.AtributoTemplate.loadTemplates();
		
		TemplateLoader.ParecerTemplate.loadTemplates();
		TemplateLoader.PontuacaoTemplate.loadTemplates();
		TemplateLoader.NotaTemplate.loadTemplates();
		TemplateLoader.RegraTemplate.loadTemplates();
		
		TemplateLoader.ResolucaoTemplate.loadTemplates();
	}

	private static class RadocTemplate {
		public static void loadTemplates() {
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

	private static class RelatoTemplate {
		public static void loadTemplates() {
			Map<String, Valor> valores = new HashMap<String, Valor>();
			valores.put("nome", new Valor("Fulano da Silva"));
			valores.put("cargaHoraria", new Valor(32));
			
			Map<String, Valor> valoresTeste = new HashMap<String, Valor>();
			valoresTeste.put("trabalho", new Valor("Pesquisa em Teste de Software"));
			valoresTeste.put("paginas", new Valor(40));

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

	private static class TipoTemplate {
		public static void loadTemplates() {
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

	private static class AtributoTemplate {
		public static void loadTemplates() {
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

	private static class ParecerTemplate {
		public static void loadTemplates() {
			Fixture.of(Parecer.class).addTemplate("parecer", new Rule() {{
				add("id", random(Long.class, range(1L, 200L)).toString());
				add("resolucao", "resolucao2013");
				add("radocs", has(4).of(Radoc.class, "radocAleatorio" ));
				add("pontuacoes", has(4).of(Pontuacao.class, "pontuacaoNotaFixa"));
				add("fundamentacao", "ficou �timo");
				add("notas", one(Nota.class, "notaPont"));
			}});
			Fixture.of(Parecer.class).addTemplate("parecerComRadocFixo", new Rule() {{
				add("id", random(Long.class, range(1L, 200L)).toString());
				add("resolucao", "resolucao2013");
				add("radocs", new ArrayList<String>().add("radocFixo"));
				add("pontuacoes", has(3).of(Pontuacao.class, "pontuacaoNotaFixa"));
				add("fundamentacao", "ficou ótimo");
				add("notas", one(Nota.class, "notaPont"));
			}});
		}
	}
	
	private static class PontuacaoTemplate {
		public static void loadTemplates() {
			Valor valor = new Valor(30);
			Fixture.of(Pontuacao.class).addTemplate("pontuacaoAleatoria", new Rule() {{
				add("atributo", random("nome", "cargaHoraria", "trabalho", "paginas") );
				add("valor", valor);
			}});
			
			Fixture.of(Pontuacao.class).addTemplate("pontuacaoNota", new Rule() {{
				add("atributo", "trabalho" );
				add("valor", valor);
			}});
			
			Fixture.of(Pontuacao.class).addTemplate("pontuacaoNotaFixa", new Rule() {{
				add("atributo", "trabalho" );
				add("valor", valor);
			}});
		}
	}
	
	private static class NotaTemplate {
		public static void loadTemplates() {
			Fixture.of(Nota.class).addTemplate("notaPont", new Rule() {{
				add("original", one(Pontuacao.class, "pontuacaoNota") );
				add("novo", one(Pontuacao.class, "pontuacaoNota"));
				add("justificativa", "mudei pq eu quis");
			}});
			
			Fixture.of(Nota.class).addTemplate("notaOriginal", new Rule() {{
				add("original", one(Pontuacao.class, "pontuacaoNotaFixa") );
				add("novo", one(Pontuacao.class, "pontuacaoNota"));
				add("justificativa", "mudei pq eu quis");
			}});
		}
	}
	
	private static class RegraTemplate {
		public static void loadTemplates() {
			// Fixture.of(Client.class).addTemplate("geekClient", newRule() {{
			// ... }})
		}
	}

	private static class ResolucaoTemplate {
		public static void loadTemplates() {
			// Fixture.of(Client.class).addTemplate("geekClient", newRule() {{
			// ... }})
		}
	}

}
