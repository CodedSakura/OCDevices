package ben_mkiv.ocdevices.common.blocks;

import ben_mkiv.ocdevices.OCDevices;
import ben_mkiv.ocdevices.common.integration.MCMultiPart.MultiPartHelper;
import ben_mkiv.ocdevices.common.tileentity.ColoredTile;
import ben_mkiv.ocdevices.common.tileentity.IUpgradeBlock;
import ben_mkiv.ocdevices.common.tileentity.TileEntityCase;
import com.google.common.base.Optional;
import li.cil.oc.common.Tier;
import li.cil.oc.common.block.Case;
import li.cil.oc.common.block.property.PropertyRotatable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class BlockCase extends Case {
    public static final int tier = Tier.Three();
    public static final int GUI_ID = 3;

    public static final caseTierProperty caseTier = new caseTierProperty();

    public BlockCase(String caseName){
        super(tier);
        setRegistryName(OCDevices.MOD_ID, caseName);
        setUnlocalizedName(caseName);
        setCreativeTab(OCDevices.creativeTab);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion){
        TileEntityCase caseTile = getTileEntity(world, pos);
        return caseTile != null ? caseTile.getExplosionResistance() : super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    @Deprecated
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos){
        TileEntityCase caseTile = getTileEntity(worldIn, pos);
        return caseTile != null ? caseTile.getHardness() : super.getBlockHardness(blockState, worldIn, pos);
    }

    @Deprecated
    @Override
    public boolean isFullBlock(IBlockState state){
        return true;
    }

    @Deprecated
    @Override
    public boolean isFullCube(IBlockState state){
        return false;
    }

    @Override
    @Deprecated
    public boolean isBlockNormalCube(IBlockState state) {
        return true;
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {

        if(ColoredTile.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ))
            return true;

        if(IUpgradeBlock.onBlockActivated(world, pos, player, hand))
            return true;

        TileEntityCase caseTile = MultiPartHelper.getCaseFromTile(world.getTileEntity(pos));
        if (caseTile != null) {
            player.openGui(OCDevices.INSTANCE, GUI_ID, MultiPartHelper.getRealWorld(caseTile), pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    private static TileEntityCase getTileEntity(IBlockAccess world, BlockPos pos){
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileEntityCase ? (TileEntityCase) te : null;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PropertyRotatable.Facing()).getHorizontalIndex() << 2 | state.getValue(caseTier);
    }

    @Deprecated
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(PropertyRotatable.Facing(), EnumFacing.getHorizontal(meta >> 2)).withProperty(caseTier, meta & 3);
    }


    @Override
    public @Nonnull IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand){
        EnumFacing yaw = EnumFacing.fromAngle(placer.rotationYaw).getOpposite();
        return getDefaultState().withProperty(PropertyRotatable.Facing(), yaw).withProperty(caseTier, Tier.One());
    }

    @Override
    public BlockStateContainer createBlockState() {
        ArrayList<IProperty> properties = new ArrayList<IProperty>(super.createBlockState().getProperties());

        IProperty[] props = new IProperty[properties.size()+1];
        for(int i=0; i < properties.size(); i++)
            props[i] = properties.get(i);

        props[properties.size()] = caseTier;

        return new ExtendedBlockState(this, props, new IUnlistedProperty[]{});
    }

    // avoid to connect to fences/glass panes
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.CENTER;
    }

}
