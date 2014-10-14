/**
 *  FlamingArrows
 *  Copyright (C) 2014 Sindastra. All rights reserved.
 *  https://github.com/sindastra/FlamingArrows
 * 
 * FlamingArrows is a simple Bukkit plugin to add functionality to bow's enchantments.
 * 
 * The above copyright notice and this notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author Sindastra <sindastra+github@gmail.com>
 * @copyright (C) 2014 Sindastra. All rights reserved.
 */

package com.github.sindastra.FlamingArrows;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	public boolean debug = true;
	
	public void sendDebugMessage(String m) {
		if(debug)
			getLogger().info( "[Debug] " + m );
	}
	
	public void sendMessageToPlayer(Player player, String msg) {
		player.sendMessage(ChatColor.GREEN + "[FlamingArrows] " + ChatColor.WHITE + msg);
	}
	
	@Override
	public void onEnable() {
		getLogger().info("Flaming arrows shall burn your shed!");
		getServer().getPluginManager().registerEvents(this, this);		
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Awwww.. no more burning sheds..");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		
		sendDebugMessage("A projectile hit something!");
		
		if( e.getEntity() instanceof Arrow ) {
			
			sendDebugMessage("Projectile is an arrow!");
			Arrow arrow = (Arrow) e.getEntity();
			
			if( arrow.getShooter() instanceof Player ) {
				
				sendDebugMessage("Shooter is a player!");
				
				Player player = (Player) arrow.getShooter();
				Location loc = (Location) arrow.getLocation();
				World world = (World) arrow.getWorld();
				
				ItemStack currentItem = player.getItemInHand();
				boolean hasFireEnchantment = currentItem.containsEnchantment(Enchantment.ARROW_FIRE);
				boolean hasPowerEnchantment = currentItem.containsEnchantment(Enchantment.ARROW_DAMAGE);
				
				if( hasFireEnchantment ) {
										
					sendDebugMessage("Bow contains 'Flame' enchantment!");					
					world.strikeLightning(loc);
					sendDebugMessage("Struck lightning at " + loc.toString());
					arrow.remove();
					
				}
				
				if( hasPowerEnchantment ) {
					
					sendDebugMessage("Bow contains 'Power' enchantment!");
					
					int powerLevel = currentItem.getEnchantmentLevel(Enchantment.ARROW_DAMAGE);	
					sendDebugMessage("'Power' enchantment has level " + powerLevel);
					
					float knockbackStrength = (float) arrow.getKnockbackStrength();
					sendDebugMessage("'Knockback Strength' is " + knockbackStrength);
					
					float explosionStrength = powerLevel / 1.5f * (knockbackStrength + 1);
					sendDebugMessage("Created explosion with strength " + explosionStrength + ", Fire: " + hasFireEnchantment);
					
					world.createExplosion( loc, explosionStrength, hasFireEnchantment );
					arrow.remove();
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent e){
		
		sendDebugMessage("----------------------------");
		sendDebugMessage("Something/one shot an arrow!");
		
		if( e.getEntity() instanceof Player) {
			
			sendDebugMessage("The shooter was a player!");
			
			Player player = (Player) e.getEntity();
			ItemStack bow = (ItemStack) e.getBow();
			
			if( bow.containsEnchantment(Enchantment.ARROW_FIRE) || bow.containsEnchantment(Enchantment.ARROW_DAMAGE) )
				sendMessageToPlayer(player, "Let your bow in your hand to use enchantments!");			
				
		}		
		
	}
	
}
