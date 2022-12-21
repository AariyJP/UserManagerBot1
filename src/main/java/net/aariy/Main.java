package net.aariy;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


public class Main extends ListenerAdapter
{
    public static void main(String[] args) throws IOException
    {
        JDA jda = JDABuilder.createDefault(args[0]).enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.updateCommands().addCommands(
                Commands.slash("panel", "キーワードを入力するパネルを設置します。（管理者用）").setDefaultPermissions(DefaultMemberPermissions.DISABLED)
        ).queue();
        jda.addEventListener(new Main());


    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e)
    {
        if(e.getName().equals("panel"))
        {
            e.reply(":white_check_mark:").setEphemeral(true).queue();
            e.getChannel().sendMessageComponents(ActionRow.of(Button.primary("keyword", "キーワードで認証"))).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent e)
    {
        if(e.getComponentId().equals("keyword"))
        {
            e.replyModal(Modal.create("keyword_modal", "キーワードを入力して認証").addActionRow(TextInput.create("keyword_input", "キーワード", TextInputStyle.SHORT).setRequired(true).build()).build()).queue();
        }
    }
    @Override
    public void onModalInteraction(ModalInteractionEvent e)
    {
        if(e.getModalId().equals("keyword_modal"))
        {
            if(e.getValue("keyword_input").equals("応援"))
            {
                e.getGuild().addRoleToMember(e.getUser(), e.getGuild().getRoleById("1055130068587925505")).queue();
                e.reply("> :white_check_mark: 認証に成功しました。").setEphemeral(true).queue();
            }
            else
            {
                e.reply("> :no_entry_sign: 認証に失敗しました。").setEphemeral(true).queue();
            }
        }
    }
}