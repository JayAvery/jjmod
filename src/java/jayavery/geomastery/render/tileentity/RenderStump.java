/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.render.tileentity;

import java.util.Map.Entry;
import org.lwjgl.opengl.GL11;
import jayavery.geomastery.blocks.BlockTree;
import jayavery.geomastery.tileentities.TEStump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TEST
public class RenderStump extends TileEntitySpecialRenderer<TEStump> {

    @Override
    public void renderTileEntityAt(TEStump te, double x, double y,
            double z, float tick, int damage) {

        IBlockState state = te.getWorld().getBlockState(te.getPos()).getActualState(te.getWorld(), te.getPos());
        EnumFacing fallDir = /*state.getValue(BlockFacing.FACING)*/ EnumFacing.EAST;
        BlockPos pos = te.getPos();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer vb = tess.getBuffer();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockModelRenderer render = dispatcher.getBlockModelRenderer();
        World world = te.getWorld();
       
        if (te.falling) {
            
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            float angle = (float) (te.prevAngle + ((te.angle - te.prevAngle) * Math.pow(1.1, tick)));
    
            
            if (fallDir == EnumFacing.NORTH) {
                
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(-angle, 1, 0, 0);
                GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
                
            } else if (fallDir == EnumFacing.SOUTH) {
                
                GlStateManager.translate(x, y, z + 1);
                GlStateManager.rotate(angle, 1, 0, 0);
                GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ() - 1);
                
            } else if (fallDir == EnumFacing.WEST) {
                
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(angle, 0, 0, 1);
                GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
                
            } else if (fallDir == EnumFacing.EAST) {
                
                GlStateManager.translate(x + 1, y, z);
                GlStateManager.rotate(-angle, 0, 0, 1);
                GlStateManager.translate(-pos.getX() - 1, -pos.getY(), -pos.getZ());
            }
            
            vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    
            for (Entry<BlockPos, IBlockState> entry : te.blocks.entrySet()) {
    
                render.renderModel(world, dispatcher.getModelForState(entry.getValue()), entry.getValue(), entry.getKey(), vb, false);
            }
            
            tess.draw();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
        
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate(x - pos.getX(), y - pos.getY(), z - pos.getZ());
        vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        render.renderModel(world, dispatcher.getModelForState(state), state, pos, vb, false);
        tess.draw();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
