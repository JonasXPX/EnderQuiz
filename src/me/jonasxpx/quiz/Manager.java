package me.jonasxpx.quiz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Manager {
	
	private static Random rm = new Random();
	private static boolean okForGo = false;
	private static ArrayList<Player> jogadores = new ArrayList<Player>();
	public static int maxPerguntas;
	public static int moneyForPay;
	
	public static Collection<String> loadPerguntas(JavaPlugin jp){
		Collection<String> p = new ArrayList<String>();
		for(String string : jp.getConfig().getStringList("Perguntas"))
			p.add(ChatColor.translateAlternateColorCodes('&', string));
		
		return p;
	}
	
	private static boolean hasNextQuestion(Collection<String> collection){
		if(collection.isEmpty() || collection.size() <= 0)
			return false;
		else
			return true;
	}
	
	public static Pergunta nextQuestion(ArrayList<String> perguntas){
		if(!hasNextQuestion(perguntas) || maxPerguntas <= 0){
			Bukkit.broadcastMessage("§dFim do evento");
			okForGo = false;
			return null;
		}
		okForGo = true;
		int next = rm.nextInt(perguntas.size());
		Pergunta pergunta = new Pergunta(perguntas.get(next));
		perguntas.remove(next);
		maxPerguntas--;
		return pergunta;
	}
	
	
	public static boolean isCorrect(Pergunta pergunta, Player player, String resposta){
		if(pergunta.getResposta().equalsIgnoreCase(resposta) || (pergunta.getRespostaList().contains(resposta.toLowerCase()))){
			return true;
		}else
			return false;
	}
	public static boolean okForGo(){
		return okForGo;
	}
	
	public static void sendMensager(String msg){
		if(!Quiz.sendForAllServer)
			Bukkit.broadcastMessage(msg);
		else{
			for(Player player : jogadores){
				if(player.isOnline())
					player.sendMessage(msg);
			}
		}
	}
	
	public static void novoParticipante(Player player){
		if(jogadores.contains(player)){
			player.sendMessage("§6Você ja entrou!.");
			return;
		}
		jogadores.add(player);
		player.sendMessage("§6Agora você está no evento!.");
	}
	
	public static void addPlayerPoints(Player player){
		player.playSound(player.getLocation(), Sound.FIRE_IGNITE, 1, 1);
		Quiz.evento.quizList.add(player.getName());
		if(Quiz.evento.quiz.containsKey(player)){
			Quiz.evento.quiz.replace(player, Quiz.evento.quiz.get(player)+1);
		}else{
			Quiz.evento.quiz.put(player, 1);
		}
	}
	
	public static void pagarJogadores(Map<Player, Integer> jogadores){
		for(Player player : jogadores.keySet()){
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
			Quiz.economy.depositPlayer(player, moneyForPay * jogadores.get(player));
			player.sendMessage("§b§m------------------§d[Quiz]§b§m------------------\n"
					+ "§bVocê ganhou §6"+moneyForPay * jogadores.get(player)+ " §bParticipando do Quiz\n"
							+ "§bE acertou §6" + jogadores.get(player)+ " §bPerguntas \n"
									+ "§b§m------------------§d[Quiz]§b§m------------------\n"
					);
		}
	}
	
}

