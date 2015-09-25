package me.jonasxpx.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Evento {
	
	private Quiz plugin;
	private boolean inGame;
	private ArrayList<String> perguntas = null;
	private BukkitTask threadStart;
	private BukkitTask threadPergunta;
	private Pergunta pergunta;
	private boolean nextPergunta;
	public boolean accept;
	public boolean valido = false;
	private int maxPerguntas;
	private int count;
	private int defaultCount;
	private int delay;
	public ArrayList<String> quizList = new ArrayList<String>();
	public Map<Player, Integer> quiz = new HashMap<Player, Integer>();
	private Listeners listener = null;
	
	public Evento(Quiz plugin, int maxPerguntas, int count, int delay) {
		this.plugin = plugin;
		this.inGame = false;
		this.nextPergunta = false;
		this.perguntas = new ArrayList<String>();
		this.perguntas.addAll(Manager.loadPerguntas(plugin));
		this.maxPerguntas = maxPerguntas;
		this.count = count;
		this.defaultCount = count;
		this.delay = delay;
	}
	
	public boolean commandStart(CommandSender player){
		if(inGame){
			player.sendMessage("�cEvento ja esta rodando!.");
			return true;
		}
		this.plugin.getServer().broadcastMessage(
				"�d�m--------------------------------------------------\n"
				+ "�dEvento quiz, responda as perguntas e ganhe money\n"
				+ "�dPara responder use �6/quiz (resposta)�d, Boa sorte!\n");
		if(plugin.sendForAllServer)this.plugin.getServer().broadcastMessage("�dUse �6/quiz jogar,�d para poder ver as perguntas em andamento!.");
		this.plugin.getServer().broadcastMessage("�d�m------------------------------------------------\n");
		inGame = true;
		listener = new Listeners();
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
		new BukkitRunnable() {
			@Override
			public void run() {
				start();
			}
		}.runTaskLater(plugin, 20 * 20);
		
		return false;
	}

	public void start(){
		
		this.threadStart = new BukkitRunnable() {
			@Override
			public void run() {
				if(count == 0){
					valido = false;
					if(quizList.isEmpty())
						Manager.sendMensager("�b�m------------------�d[Quiz]�b�m------------------\n"
								+ "�6Ningu�m acertou =`( \n"
								+ "�b�m------------------�d[Quiz]�b�m------------------\n"
								);
					else
						Manager.sendMensager("�b�m------------------�d[Quiz]�b�m------------------\n"
								+ "�6" + quizList.size() + " �bJogador(es) acertaram\n"
								+ "�b�m------------------�d[Quiz]�b�m------------------\n");
					Manager.sendMensager("�7O chat foi liberado");
					new BukkitRunnable() {
						@Override
						public void run() {
							continuar();
						}
					}.runTaskLaterAsynchronously(plugin, 20 * 8);
				}
				if(nextPergunta){
					return;
				}
				pergunta = Manager.nextQuestion(perguntas);
				if(!Manager.okForGo()){
					parar();
					return;
				}
				quizList.clear();
				if(Quiz.sendForAllServer)Bukkit.broadcastMessage("�dEvento quiz em andamento, /quiz jogar");
				threadPergunta = new BukkitRunnable() {
					@Override
					public void run() {
						nextPergunta = true;
						valido = true;
						if((!Manager.okForGo()) || pergunta.getPergunta() == null){
							parar();
							return;
						}
						accept = false;
						Manager.sendMensager(
								"�b�m----------------------�d[Quiz]�b�m----------------------\n"
								+ "�6"+ pergunta.getPergunta() +"\n"
								+ "�b Responda com /quiz (resposta), Avisos: " + count
								+"\n"
								+ "�b�m----------------------�d[Quiz]�b�m----------------------\n");
						count--;
					}
				}.runTaskTimerAsynchronously(plugin, 0, 20 * delay);
				
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20 * 15);
	}
	
	public void continuar(){
		this.nextPergunta=false;
		this.count = this.defaultCount;
		if(threadPergunta != null)threadPergunta.cancel();
	}
	
	public void parar(){
		Manager.pagarJogadores(quiz);
		this.nextPergunta=false;
		this.inGame=false;
		quizList.clear();
		quiz.clear();
		Manager.getJogadores().clear();
		this.count = this.defaultCount;
		threadPergunta.cancel();
		threadStart.cancel();
		Manager.maxPerguntas = maxPerguntas;
		HandlerList.unregisterAll(listener);
		listener = null;
	}

	
	public Pergunta getPergunta(){
		return pergunta;
	}
}
