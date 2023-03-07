import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.exceptions.ParsingException;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.MiscUtil;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.String;
import java.util.function.UnaryOperator;


public class TestableDesignTest
{
    @Test
    public void testGetNewNormal()
    {
        CommandData command = new CommandDataImpl("ban", "Ban a user from this server")
                .setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                .addOption(OptionType.USER, "user", "The user to ban", true)
                .addOption(OptionType.STRING, "reason", "The ban reason");

        DataObject data = command.toData();
        DataArray options = data.getArray("options");

        //test if getting string value works
        DataObject option = options.getObject(0);
        Assertions.assertEquals("user", option.getNew(String.class, "name", null, null));
        Assertions.assertNull(option.getNew(String.class, "nonexistentKey", null, null));

        //test if getting integer value works
        OptionData optionTest = new OptionData(OptionType.STRING, "example_option", "this is an option!");
        optionTest.setMinLength(2);
        DataObject op = optionTest.toData();
        Assertions.assertEquals(2,op.getNew(Integer.class, "min_length", null, null));

        //test if typecasting to string is working
        Assertions.assertEquals(String.class, op.getNew(String.class, "min_length", null, null).getClass());
    }

    @Test
    public void testGetNewCoercionNormal()
    {
        OptionData optionTest = new OptionData(OptionType.STRING, "example_option", "this is an option!");
        optionTest.setMinLength(2);
        DataObject op = optionTest.toData();

        //test that converting to long from int works
        Assertions.assertEquals(Long.class, op.getNewCoercion(Long.class, "min_length", 2, MiscUtil::parseLong, Number::longValue).getClass());

        //test that converting to double from int works
        Assertions.assertEquals(Double.class, op.getNewCoercion(Double.class, "min_length",2, Double::parseDouble, Number::doubleValue).getClass());

        //test that converting to string from int works
        Assertions.assertEquals("2", op.getNewCoercion(String.class, "min_length",2, UnaryOperator.identity(), String::valueOf));
    }

    @Test
    public void testGetNewCoercionException()
    {
        OptionData optionTest = new OptionData(OptionType.STRING, "example_option", "this is an option!");
        optionTest.setMinLength(2);
        DataObject op = optionTest.toData();

        Assertions.assertThrows(ParsingException.class, () -> op.getNewCoercion(Integer.class, "min_length", 2, null, null));

    }

}
