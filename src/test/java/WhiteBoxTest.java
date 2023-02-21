import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * expand testing coverage to include api/interactions/build/commands
 */
public class WhiteBoxTest
{
    @Test
    public void testOptionDataGetDescription()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");
        Assertions.assertEquals("this is an option!", option.getDescription());
    }

    @Test
    public void testOptionDataAutocomplete()
    {
        OptionData notAutocompleteOption = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");
        Assertions.assertFalse(notAutocompleteOption.isAutoComplete());

        OptionData isAutocompleteOption = new OptionData(OptionType.INTEGER, "example_option_two", "this is an second option!", false, true);
        Assertions.assertTrue(isAutocompleteOption.isAutoComplete());
    }

    @Test
    public void testOptionDataMinimumValue()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");
        option.setMinValue(10L);
        Assertions.assertEquals(10L,option.getMinValue());

        OptionData optionTwo = new OptionData(OptionType.NUMBER, "example_option_two", "this is an second option!");
        optionTwo.setMinValue(1.99);
        Assertions.assertEquals( 1.99,optionTwo.getMinValue());

    }

    @Test
    public void testOptionDataMaximumValue()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");
        option.setMaxValue(10L);
        Assertions.assertEquals(10L,option.getMaxValue());

        OptionData optionTwo = new OptionData(OptionType.NUMBER, "example_option_two", "this is an second option!");
        optionTwo.setMaxValue(1.99);
        Assertions.assertEquals( 1.99,optionTwo.getMaxValue());
    }

    @Test
    public void testOptionDataRangeValue()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");
        option.setRequiredRange(-10L,10L);
        Assertions.assertEquals(-10L, option.getMinValue());
        Assertions.assertEquals(10L,option.getMaxValue());


        OptionData optionTwo = new OptionData(OptionType.NUMBER, "example_option_two", "this is an second option!");
        optionTwo.setRequiredRange(1.99,29.99);
        Assertions.assertEquals( 1.99,optionTwo.getMinValue());
        Assertions.assertEquals(29.99, optionTwo.getMaxValue());
    }

    @Test
    public void testOptionDataMinimumLength()
    {
        OptionData option = new OptionData(OptionType.STRING, "example_option", "this is an option!");
        option.setMinLength(2);
        Assertions.assertEquals(2,option.getMinLength());
    }

    @Test
    public void testOptionDataMaximumLength()
    {
        OptionData option = new OptionData(OptionType.STRING, "example_option", "this is an option!");
        option.setMaxLength(10);
        Assertions.assertEquals(10,option.getMaxLength());
    }

    @Test
    public void testOptionDataRangeLength()
    {
        OptionData option = new OptionData(OptionType.STRING, "example_option", "this is an option!");
        option.setRequiredLength(15,30);
        Assertions.assertEquals(15, option.getMinLength());
        Assertions.assertEquals(30,option.getMaxLength());
    }

    @Test
    public void testOptionDataAddChoice()
    {
        OptionData option = new OptionData(OptionType.NUMBER, "example_option", "this is an option!");
        Assertions.assertThrows(IllegalArgumentException.class, () -> option.addChoice("", 49.99));
        Assertions.assertThrows(IllegalArgumentException.class, () -> option.addChoice("valid_name", ""));


        Command.Choice ch = new Command.Choice("valid_name", 49.99);
        List<Command.Choice> choices = new ArrayList<>();
        choices.add(ch);

        option.addChoice("valid_name", 49.99);
        Assertions.assertEquals(choices, option.getChoices());
    }

    @Test
    public void testOptionDataAddManyChoices()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");

        List<Command.Choice> choices = new ArrayList<>();
        for (int i = 0; i < 25; i++)
        {
            choices.add(new Command.Choice("choice_" + i, i));
        }
        option.addChoices(choices);
        Assertions.assertEquals(choices, option.getChoices());
    }

    @Test
    public void testOptionDataFromData()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");

        DataObject object = option.toData();
        OptionData convertedData = OptionData.fromData(object);

        Assertions.assertEquals("example_option", convertedData.getName());
        Assertions.assertEquals("this is an option!", convertedData.getDescription());
    }

    @Test
    public void testOptionDataFromOption()
    {
        OptionData option = new OptionData(OptionType.INTEGER, "example_option", "this is an option!");
        DataObject object = option.toData();

        Command.Option cOption = new Command.Option(object);
        OptionData optionData = OptionData.fromOption(cOption);

        Assertions.assertEquals("example_option", optionData.getName());
        Assertions.assertEquals("this is an option!", optionData.getDescription());
    }

}
