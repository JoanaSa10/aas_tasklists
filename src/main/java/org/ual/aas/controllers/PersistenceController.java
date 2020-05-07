package org.ual.aas.controllers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.ual.aas.models.Task;
import org.ual.aas.models.TaskList;

public class PersistenceController {
	public static void main(String[] args) {
		// Criar uma lista
		TaskList taskList = new TaskList("Lista de exemplo");
		
		// Adicionar tarefas
		taskList.addTask(new Task("Primeira tarefa."));
		taskList.addTask(new Task("Segunda tarefa."));
		taskList.addTask(new Task("Terceira tarefa."));
		
		// Gravar a lista
		SessionFactory sessionFactory = null;
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure("resources/hibernate.cfg.xml")
				.build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(taskList);
		session.getTransaction().commit();
		session.close();
		
		// Ler as listas
		session = sessionFactory.openSession();
		session.beginTransaction();
		List result = session.createQuery("from TaskList").list();

		for(TaskList list : (List<TaskList>)result) {
			System.out.println(list.getName());
			for(Task task : list.getTasks()) {
				System.out.println(task.getDescription());
			}
		}
		// Listas as tarefas
		
		session.getTransaction().commit();
		session.close();	
	
		sessionFactory.close();
	}
}

