package br.edu.ifpb.resteasyapp.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.edu.ifpb.resteasyapp.dao.ProfessorDAO;
import br.edu.ifpb.resteasyapp.entidade.Professor;

@Path("professor")
public class ProfessorController {

	/**
	 * Cadastra o professor no sistema.
	 * 
	 * @param professor
	 * @return Response
	 */

	@PermitAll
	@POST
	@Path("/inserir")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insert(Professor professor) {

		// Preparando a resposta. Provisoriamente o sistema preparar� a resposta
		// como requisi��o incorreta.
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		// TODO: Regra de neg�cio e manipula��o de dados nesse ponto. As
		// informa��os devem ser associadas
		// nesse ponto ao biuld (response).
		
		

		try {

			int id = ProfessorDAO.getInstance().insert(professor);

			professor.setId(id);

			builder.status(Response.Status.CREATED).entity(professor);

		} catch (SQLException e) {

			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		// Resposta.
		return builder.build();
	}

	/**
	 * Retorna todos os professores cadastrados.
	 * 
	 * @return Response
	 */
	@PermitAll
	@GET
	@Path("/listar")
	@Produces("application/json")
	public List<Professor> getAll() {

		// Retorno em formato de lista.
		// Desse modo o response sempre conter� o c�digo de resposta OK.
		List<Professor> professores = new ArrayList<Professor>();

		try {

			// TODO: Regra de neg�cio e manipula��o de dados nesse ponto.
			professores = ProfessorDAO.getInstance().getAll();

		} catch (SQLException e) {

			// TODO: Tratar a exce��o.
		}

		// Ser� retornado ao cliente um conjunto de alunos no formato de Json.
		return professores;
	}

	/**
	 * Recupera o professor cadastrado no sistema atrav�s do seu id.
	 * 
	 * @param id
	 * @return Response
	 */
	@PermitAll
	@GET
	@Path("/id_professor/{id_professor}")
	@Produces("application/json")
	public Response getProfessorById(@PathParam("id_professor") int id) {

		// Preparando a resposta. Provisoriamente o sistema preparar� a resposta
		// como requisi��o incorreta.
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		try {

			// Regra de neg�cio e manipula��o de dados nesse ponto.
			Professor professor = ProfessorDAO.getInstance().getById(id);

			if (professor != null) {

				// As informa��os associadas ao build para o response.
				builder.status(Response.Status.OK);
				builder.entity(professor);

			} else {

				// Conte�do n�o encontrado.
				builder.status(Response.Status.NOT_FOUND);
			}

		} catch (SQLException exception) {

			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		// Resposta
		return builder.build();
	}

	@PermitAll
	@POST
	@Path("/deletar")
	@Consumes("application/json")
	@Produces("application/json")
	public Response delete(Professor professor) {
		// Preparando a resposta. Provisoriamente o sistema preparar� a resposta
		// como requisi��o incorreta.
		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		try {
			
			if (professor != null) {
				// Regra de neg�cio e manipula��o de dados nesse ponto.
				int cod_u = ProfessorDAO.getInstance().findProfessorByEmail(professor.getEmail()).getId();
				ProfessorDAO.getInstance().delete(cod_u);
				Professor professorTeste = ProfessorDAO.getInstance().findProfessorByEmail(professor.getEmail());

				if (professorTeste == null) {

					// As informa��os associadas ao build para o response.
					builder.status(Response.Status.NO_CONTENT);

				} else {

					// Conte�do n�o deletado
					builder.status(Response.Status.NOT_IMPLEMENTED).entity(professorTeste);
				}
			}

		} catch (SQLException exception) {

			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		// Resposta
		return builder.build();
	}

	@PermitAll
	@POST
	@Path("/alterar")
	@Produces("application/json")
	@Consumes("application/json")
	public Response update(Professor professor) {

		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		try {

			int cod_u = ProfessorDAO.getInstance().findProfessorByEmail(professor.getEmail()).getId();
			professor.setId(cod_u);
			ProfessorDAO.getInstance().updateByEntity(professor);
			builder.status(Response.Status.OK).entity(professor);

		} catch (SQLException exception) {
			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
		}
		return builder.build();

	}

	@PermitAll
	@POST
	@Path("/login")
	@Produces("application/json")
	@Consumes("application/json")
	public Response login(Professor professorEnviado) {

		ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		builder.expires(new Date());

		try {

			Professor professorRecebido = ProfessorDAO.getInstance().findProfessorByEmail(professorEnviado.getEmail());

			
			if (professorRecebido.getSenha().equals(professorEnviado.getSenha())){
				
				builder.status(Response.Status.OK).entity(professorRecebido);
			
			} else {
				
				if (professorRecebido.getSenha() == null || professorRecebido.getSenha().isEmpty()){
				
					builder.status(Response.Status.EXPECTATION_FAILED).entity(professorRecebido);
				
				} else {
					
					builder.status(Response.Status.BAD_REQUEST).entity(professorRecebido);
				}
			}

		} catch (SQLException exception) {
			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
		}

		return builder.build();
	}

}