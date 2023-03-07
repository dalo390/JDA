

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;


public class SlasherTest {
    @Test
    public void testSlashCommandAssignUserRole(){
        CommandData command = new CommandDataImpl("assignrole", "assign user a role")
                .addOptions(new OptionData(OptionType.USER, "user","the user to give role to",  true))
                .addOptions(new OptionData(OptionType.ROLE, "role", "the role to be given", true));
        DataObject data = command.toData();
        Assertions.assertEquals("assignrole", data.getString("name"));
        Assertions.assertEquals("assign user a role", data.getString("description"));

        DataArray options = data.getArray("options");
        DataObject option = options.getObject(0);
        Assertions.assertTrue(option.getBoolean("required"));
        Assertions.assertEquals("user", option.getString("name"));
        Assertions.assertEquals("the user to give role to", option.getString("description"));

        option = options.getObject(1);
        Assertions.assertTrue(option.getBoolean("required"));
        Assertions.assertEquals("role", option.getString("name"));
        Assertions.assertEquals("the role to be given", option.getString("description"));

    }

    @Test
    public void testSlashSay(){
        CommandData command = new CommandDataImpl("say", "make the bot say what you want")
                .addOptions(new OptionData(OptionType.STRING, "content", "What the bot should say", true));
        DataObject data = command.toData();
        Assertions.assertEquals("say", data.getString("name"));
        Assertions.assertEquals("make the bot say what you want", data.getString("description"));

        DataArray options = data.getArray("options");
        DataObject option = options.getObject(0);
        option = options.getObject(0);
        
        Assertions.assertTrue(option.getBoolean("required"));
        Assertions.assertEquals("content", option.getString("name"));
        Assertions.assertEquals("What the bot should say", option.getString("description"));

    }


    @Test
    public void testSlashLeave(){
        CommandData command = new CommandDataImpl("prune", "prune messages from the channel")
        .setGuildOnly(true)
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE))
        .addOptions(new OptionData(OptionType.INTEGER, "amount", "How many messages to prune (Default 100)", true));
        DataObject data = command.toData();
        Assertions.assertEquals("prune", data.getString("name"));
        Assertions.assertEquals("prune messages from the channel", data.getString("description"));

        DataArray options = data.getArray("options");
        DataObject option = options.getObject(0);
        Assertions.assertTrue(option.getBoolean("required"));
        Assertions.assertEquals("amount", option.getString("name"));
        Assertions.assertEquals("How many messages to prune (Default 100)", option.getString("description"));

    }


    @Test
    public void testLeavePermissions()
    {
        CommandData command = new CommandDataImpl("leave", "make the bot leave the server")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        DataObject data = command.toData();

        Assertions.assertEquals(0, data.getUnsignedLong("default_member_permissions"));

        command.setDefaultPermissions(DefaultMemberPermissions.ENABLED);
        data = command.toData();
        Assertions.assertTrue(data.isNull("default_member_permissions"));
    }

    @Test
    public void testPrunePermissions()
    {
        CommandData command = new CommandDataImpl("prune", "prune messages from the channel")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        DataObject data = command.toData();

        Assertions.assertEquals(0, data.getUnsignedLong("default_member_permissions"));

        command.setDefaultPermissions(DefaultMemberPermissions.ENABLED);
        data = command.toData();
        Assertions.assertTrue(data.isNull("default_member_permissions"));
    }



    @Test
    public void testBanCommannds()
    {
        CommandData command = new CommandDataImpl("ban", "Ban a user from this server")
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                .addOption(OptionType.USER, "user", "The user to ban", true) // required before non-required
                .addOption(OptionType.STRING, "reason", "The ban reason") // test that default is false
                .addOption(OptionType.INTEGER, "days", "The duration of the ban", false); // test with explicit false

        DataObject data = command.toData();
        Assertions.assertEquals("ban", data.getString("name"));
        Assertions.assertEquals("Ban a user from this server", data.getString("description"));
        Assertions.assertFalse(data.getBoolean("dm_permission"));
        Assertions.assertEquals(Permission.BAN_MEMBERS.getRawValue(), data.getUnsignedLong("default_member_permissions"));

        DataArray options = data.getArray("options");

        DataObject option = options.getObject(0);
        Assertions.assertTrue(option.getBoolean("required"));
        Assertions.assertEquals("user", option.getString("name"));
        Assertions.assertEquals("The user to ban", option.getString("description"));

        option = options.getObject(1);
        Assertions.assertFalse(option.getBoolean("required"));
        Assertions.assertEquals("reason", option.getString("name"));
        Assertions.assertEquals("The ban reason", option.getString("description"));

        option = options.getObject(2);
        Assertions.assertFalse(option.getBoolean("required"));
        Assertions.assertEquals("days", option.getString("name"));
        Assertions.assertEquals("The duration of the ban", option.getString("description"));
    }

    @Test
    public void testDefaultMemberPermissions()
    {
        CommandData command = new CommandDataImpl("ban", "Ban a user from this server")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED);
        DataObject data = command.toData();

        Assertions.assertEquals(0, data.getUnsignedLong("default_member_permissions"));

        command.setDefaultPermissions(DefaultMemberPermissions.ENABLED);
        data = command.toData();
        Assertions.assertTrue(data.isNull("default_member_permissions"));
    }

    @Test
    public void testSubcommand()
    {
        CommandDataImpl command = new CommandDataImpl("mod", "Moderation commands")
                .addSubcommands(new SubcommandData("ban", "Ban a user from this server")
                    .addOption(OptionType.USER, "user", "The user to ban", true) // required before non-required
                    .addOption(OptionType.STRING, "reason", "The ban reason") // test that default is false
                    .addOption(OptionType.INTEGER, "days", "The duration of the ban", false)); // test with explicit false

        DataObject data = command.toData();
        Assertions.assertEquals("mod", data.getString("name"));
        Assertions.assertEquals("Moderation commands", data.getString("description"));

        DataObject subdata = data.getArray("options").getObject(0);
        Assertions.assertEquals("ban", subdata.getString("name"));
        Assertions.assertEquals("Ban a user from this server", subdata.getString("description"));

        DataArray options = subdata.getArray("options");

        DataObject option = options.getObject(0);
        Assertions.assertTrue(option.getBoolean("required"));
        Assertions.assertEquals("user", option.getString("name"));
        Assertions.assertEquals("The user to ban", option.getString("description"));

        option = options.getObject(1);
        Assertions.assertFalse(option.getBoolean("required"));
        Assertions.assertEquals("reason", option.getString("name"));
        Assertions.assertEquals("The ban reason", option.getString("description"));

        option = options.getObject(2);
        Assertions.assertFalse(option.getBoolean("required"));
        Assertions.assertEquals("days", option.getString("name"));
        Assertions.assertEquals("The duration of the ban", option.getString("description"));
    }

    @Test
    public void testSubcommandGroup()
    {
        CommandDataImpl command = new CommandDataImpl("mod", "Moderation commands")
                .addSubcommandGroups(new SubcommandGroupData("ban", "Ban or unban a user from this server")
                    .addSubcommands(new SubcommandData("add", "Ban a user from this server")
                        .addOption(OptionType.USER, "user", "The user to ban", true) // required before non-required
                        .addOption(OptionType.STRING, "reason", "The ban reason") // test that default is false
                        .addOption(OptionType.INTEGER, "days", "The duration of the ban", false))); // test with explicit false

        DataObject data = command.toData();
        Assertions.assertEquals("mod", data.getString("name"));
        Assertions.assertEquals("Moderation commands", data.getString("description"));

        DataObject group = data.getArray("options").getObject(0);
        Assertions.assertEquals("ban", group.getString("name"));
        Assertions.assertEquals("Ban or unban a user from this server", group.getString("description"));

        DataObject subdata = group.getArray("options").getObject(0);
        Assertions.assertEquals("add", subdata.getString("name"));
        Assertions.assertEquals("Ban a user from this server", subdata.getString("description"));
        DataArray options = subdata.getArray("options");

        DataObject option = options.getObject(0);
        Assertions.assertTrue(option.getBoolean("required"));
        Assertions.assertEquals("user", option.getString("name"));
        Assertions.assertEquals("The user to ban", option.getString("description"));

        option = options.getObject(1);
        Assertions.assertFalse(option.getBoolean("required"));
        Assertions.assertEquals("reason", option.getString("name"));
        Assertions.assertEquals("The ban reason", option.getString("description"));

        option = options.getObject(2);
        Assertions.assertFalse(option.getBoolean("required"));
        Assertions.assertEquals("days", option.getString("name"));
        Assertions.assertEquals("The duration of the ban", option.getString("description"));
    }

    @Test
    public void testCommandError()
    {
        CommandDataImpl command = new CommandDataImpl("ban", "Simple ban command");
        command.addOption(OptionType.STRING, "opt", "desc");

        Assertions.assertThrows(IllegalArgumentException.class, () -> command.addOption(OptionType.STRING, "other", "desc", true));       
    }


    @Test
    public void testSubCommandError(){
        SubcommandData subcommand = new SubcommandData("sub", "Simple subcommand");
        subcommand.addOption(OptionType.STRING, "opt", "desc");
        Assertions.assertThrows(IllegalArgumentException.class, () -> subcommand.addOption(OptionType.STRING, "other", "desc", true));
    }

    //no handler exception
    //name checks will check if there aren't a name, then there won't be a handler
    @Test
    public void testNameChecks()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CommandDataImpl("invalid name", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CommandDataImpl("invalidName", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CommandDataImpl("valid_name", ""));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubcommandData("invalid name", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubcommandData("invalidName", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubcommandData("valid_name", ""));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubcommandGroupData("invalid name", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubcommandGroupData("invalidName", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new SubcommandGroupData("valid_name", ""));
    }

    //invalid input types or type mismatch. Different from no handler
    @Test
    public void testChoices()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "choice", "Option with choices!");
        Assertions.assertThrows(IllegalArgumentException.class, () -> option.addChoice("invalid name", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> option.addChoice("invalidName", "Valid description"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> option.addChoice("valid_name", ""));

        List<Command.Choice> choices = new ArrayList<>();
        for (int i = 0; i < 25; i++)
        {
            option.addChoice("choice_" + i, i);
            choices.add(new Command.Choice("choice_" + i, i));
        }
        Assertions.assertThrows(IllegalArgumentException.class, () -> option.addChoice("name", 100));
        Assertions.assertEquals(25, option.getChoices().size());
        Assertions.assertEquals(choices, option.getChoices());
    }
}
