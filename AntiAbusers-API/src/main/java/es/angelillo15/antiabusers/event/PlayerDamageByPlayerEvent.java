package es.angelillo15.antiabusers.event;

import es.angelillo15.antiabusers.AntiAbuserPlayer;
import es.angelillo15.antiabusers.enums.AttackResult;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a player damages another player.
 * This event is called before the damage is applied.
 * This event is cancellable.
 */
public class PlayerDamageByPlayerEvent extends Event implements Cancellable {
  private static final HandlerList HANDLERS = new HandlerList();
  @Getter
  private final AntiAbuserPlayer damager;
  @Getter
  private final AntiAbuserPlayer damaged;
  @Getter
  private final AttackResult attackResult;
  private boolean cancelled;

  /**
   * Constructor for the event.
   * @param damager The player who is attacking.
   * @param damaged The player who is being attacked.
   * @param attackResult The result of the attack.
   */
  public PlayerDamageByPlayerEvent(AntiAbuserPlayer damager, AntiAbuserPlayer damaged, AttackResult attackResult) {
    this.damager = damager;
    this.damaged = damaged;
    this.attackResult = attackResult;
  }

  /**
   * Get the player who is attacking.
   * @return The player who is attacking.
   */
  public AntiAbuserPlayer getDamager() {
    return damager;
  }

  /**
   * Get the player who is being attacked.
   * @return The player who is being attacked.
   */
  public AntiAbuserPlayer getDamaged() {
    return damaged;
  }

  /**
   * Get the result of the attack.
   * @return The result of the attack.
   */
  public AttackResult getAttackResult() {
    return attackResult;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    cancelled = cancel;
  }

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }

}
