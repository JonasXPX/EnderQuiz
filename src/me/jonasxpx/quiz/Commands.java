package me.jonasxpx.quiz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{

	private StringBuilder sb;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length == 0){
			
		}
		if(args.length >= 1){
			if(args[0].equalsIgnoreCase("iniciar")){
				if(sender.isOp()){
					Quiz.evento.commandStart(sender);
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("jogar")){
				Manager.novoParticipante((Player)sender);
				return true;
			}
			if(Quiz.evento.accept){
				sender.sendMessage("§6Aguarde uma nova pergunta!.");
				return true;
			}
			if(Quiz.evento.getPergunta() != null){
					sb = new StringBuilder();
					for(int x = 0; x < args.length; x++){
						sb.append(args[x] + " ");
					}
					if(Manager.isCorrect(Quiz.evento.getPergunta(), (Player)sender, sb.toString().substring(0, sb.toString().length()-1))){
						if(Quiz.evento.quizList.contains(sender.getName())){
							sender.sendMessage("§6Aguarde uma nova pergunta");
							return true;
						}
						sender.sendMessage("§b§m-------------------------------------\n"
								+ "§bParabens você Acertou! §6 +1 Ponto\n"
								+ "§b§m-------------------------------------");
						Manager.addPlayerPoints((Player)sender);
						/*Manager.sendMensager("§b§m-------------------------------------\n"
								+ "§b"+sender.getName()+" Acertou!\n"
								+ "§bResposta: §6"+Quiz.evento.getPergunta().getResposta() + "\n"
								+ "§b§m-------------------------------------");
						
						Quiz.evento.continuar();
						*/
					}else{
						sender.sendMessage("§cVocê errou!.");
					}
			}
		}
		return true;
	}
	
}
