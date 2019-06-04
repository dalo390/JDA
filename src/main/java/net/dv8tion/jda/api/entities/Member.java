/*
 * Copyright 2015-2019 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.api.entities;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;

/**
 * Represents a Guild-specific User.
 *
 * <p>Contains all guild-specific information about a User. (Roles, Nickname, VoiceStatus etc.)
 *
 * @since 3.0
 */
public interface Member extends IMentionable, IPermissionHolder
{
    /**
     * The user wrapped by this Entity.
     *
     * @return {@link net.dv8tion.jda.api.entities.User User}
     */
    @Nonnull
    User getUser();

    /**
     * The Guild in which this Member is represented.
     *
     * @return {@link net.dv8tion.jda.api.entities.Guild Guild}
     */
    @Nonnull
    Guild getGuild();

    /**
     * The JDA instance.
     *
     * @return The current JDA instance.
     */
    @Nonnull
    JDA getJDA();

    /**
     * The {@link java.time.OffsetDateTime Time} this Member joined the Guild.
     *
     * @return The Join Date.
     */
    @Nonnull
    OffsetDateTime getTimeJoined();

    /**
     * The {@link net.dv8tion.jda.api.entities.GuildVoiceState VoiceState} of this Member.
     * <br><b>This will be null when the {@link net.dv8tion.jda.api.utils.cache.CacheFlag#VOICE_STATE} is disabled manually</b>
     *
     * <p>This can be used to get the Member's VoiceChannel using {@link GuildVoiceState#getChannel()}.
     *
     * @return {@link net.dv8tion.jda.api.entities.GuildVoiceState GuildVoiceState}
     */
    @Nullable
    GuildVoiceState getVoiceState();

    /**
     * The activities of the user.
     * <br>If the user does not currently have any activity, this returns an empty list.
     *
     * @return Immutable list of {@link Activity Activities} for the user
     */
    @Nonnull
    List<Activity> getActivities();

    /**
     * Returns the {@link net.dv8tion.jda.api.OnlineStatus OnlineStatus} of the User.
     * <br>If the {@link net.dv8tion.jda.api.OnlineStatus OnlineStatus} is unrecognized, will return {@link net.dv8tion.jda.api.OnlineStatus#UNKNOWN UNKNOWN}.
     *
     * @return The current {@link net.dv8tion.jda.api.OnlineStatus OnlineStatus} of the {@link net.dv8tion.jda.api.entities.User User}.
     */
    @Nonnull
    OnlineStatus getOnlineStatus();

    /**
     * The platform dependent {@link net.dv8tion.jda.api.OnlineStatus} of this member.
     * <br>Since a user can be connected from multiple different devices such as web and mobile,
     * discord specifies a status for each {@link net.dv8tion.jda.api.entities.ClientType}.
     *
     * <p>If a user is not online on the specified type,
     * {@link net.dv8tion.jda.api.OnlineStatus#OFFLINE OFFLINE} is returned.
     *
     * @param  type
     *         The type of client
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided type is null
     *
     * @return The status for that specific client or OFFLINE
     */
    @Nonnull
    OnlineStatus getOnlineStatus(@Nonnull ClientType type);

    /**
     * Returns the current nickname of this Member for the parent Guild.
     *
     * <p>This can be changed using
     * {@link net.dv8tion.jda.api.entities.Guild#modifyNickname(Member, String) modifyNickname(Member, String)}.
     *
     * @return The nickname or null, if no nickname is set.
     */
    @Nullable
    String getNickname();

    /**
     * Retrieves the Name displayed in the official Discord Client.
     *
     * @return The Nickname of this Member or the Username if no Nickname is present.
     */
    @Nonnull
    String getEffectiveName();

    /**
     * The roles applied to this Member.
     * <br>The roles are ordered based on their position. The highest role being at index 0
     * and the lowest at the last index.
     *
     * <p>A Member's roles can be changed using the <b>addRolesToMember</b>, <b>removeRolesFromMember</b>, and <b>modifyMemberRoles</b>
     * methods in {@link net.dv8tion.jda.api.entities.Guild Guild}.
     *
     * <p><b>The Public Role ({@code @everyone}) is not included in the returned immutable list of roles
     * <br>It is implicit that every member holds the Public Role in a Guild thus it is not listed here!</b>
     *
     * @return An immutable List of {@link net.dv8tion.jda.api.entities.Role Roles} for this Member.
     */
    @Nonnull
    List<Role> getRoles();

    /**
     * The {@link java.awt.Color Color} of this Member's name in a Guild.
     *
     * <p>This is determined by the color of the highest role assigned to them that does not have the default color.
     * <br>If all roles have default color, this returns null.
     *
     * @return The display Color for this Member.
     *
     * @see    #getColorRaw()
     */
    @Nullable
    Color getColor();

    /**
     * The raw RGB value for the color of this member.
     * <br>Defaulting to {@link net.dv8tion.jda.api.entities.Role#DEFAULT_COLOR_RAW Role.DEFAULT_COLOR_RAW}
     * if this member uses the default color (special property, it changes depending on theme used in the client)
     *
     * @return The raw RGB value or the role default
     */
    int getColorRaw();

    /**
     * The Permissions this Member holds in the specified {@link GuildChannel GuildChannel}.
     * <br>Permissions returned by this may be different from {@link #getPermissions()}
     * due to the GuildChannel's {@link net.dv8tion.jda.api.entities.PermissionOverride PermissionOverrides}.
     * <br><u>Changes to the returned set do not affect this entity directly.</u>
     *
     * @param  channel
     *         The {@link GuildChannel GuildChannel} of which to get Permissions for
     *
     * @throws java.lang.IllegalArgumentException
     *         If the channel is null
     *
     * @return Set of Permissions granted to this Member.
     */
    @Nonnull
    EnumSet<Permission> getPermissions(@Nonnull GuildChannel channel);

    /**
     * Whether this Member can interact with the provided Member
     * (kick/ban/etc.)
     *
     * @param  member
     *         The target Member to check
     *
     * @throws NullPointerException
     *         if the specified Member is null
     * @throws IllegalArgumentException
     *         if the specified Member is not from the same guild
     *
     * @return True, if this Member is able to interact with the specified Member
     *
     * @see    net.dv8tion.jda.internal.utils.PermissionUtil#canInteract(Member, Member)
     */
    boolean canInteract(@Nonnull Member member);

    /**
     * Whether this Member can interact with the provided {@link net.dv8tion.jda.api.entities.Role Role}
     * (kick/ban/move/modify/delete/etc.)
     *
     * @param  role
     *         The target Role to check
     *
     * @throws NullPointerException
     *         if the specified Role is null
     * @throws IllegalArgumentException
     *         if the specified Role is not from the same guild
     *
     * @return True, if this member is able to interact with the specified Role
     *
     * @see    net.dv8tion.jda.internal.utils.PermissionUtil#canInteract(Member, Role)
     */
    boolean canInteract(@Nonnull Role role);

    /**
     * Whether this Member can interact with the provided {@link net.dv8tion.jda.api.entities.Emote Emote}
     * (use in a message)
     *
     * @param  emote
     *         The target Emote to check
     *
     * @throws NullPointerException
     *         if the specified Emote is null
     * @throws IllegalArgumentException
     *         if the specified Emote is not from the same guild
     *
     * @return True, if this Member is able to interact with the specified Emote
     *
     * @see    net.dv8tion.jda.internal.utils.PermissionUtil#canInteract(Member, Emote)
     */
    boolean canInteract(@Nonnull Emote emote);

    /**
     * Checks whether this member is the owner of its related {@link net.dv8tion.jda.api.entities.Guild Guild}.
     *
     * @return True, if this member is the owner of the attached Guild.
     */
    boolean isOwner();

    /**
     * The default {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} for a {@link net.dv8tion.jda.api.entities.Member Member}.
     * <br>This is the channel that the Discord client will default to opening when a Guild is opened for the first time
     * after joining the guild.
     * <br>The default channel is the channel with the highest position in which the member has
     * {@link net.dv8tion.jda.api.Permission#MESSAGE_READ Permission.MESSAGE_READ} permissions. If this requirement doesn't apply for
     * any channel in the guild, this method returns {@code null}.
     *
     * @return The {@link net.dv8tion.jda.api.entities.TextChannel TextChannel} representing the default channel for this member
     *         or null if no such channel exists.
     */
    @Nullable
    TextChannel getDefaultChannel();
}
