param([string]$RepoPath = $PSScriptRoot)
$ErrorActionPreference = 'Stop'

function Fail([string]$Message) { throw $Message }
function Read-Source([string]$Relative) {
    $path = Join-Path $RepoPath $Relative
    if (-not (Test-Path -LiteralPath $path -PathType Leaf)) { Fail "Missing file: $Relative" }
    return [System.IO.File]::ReadAllText($path, [System.Text.Encoding]::UTF8).Replace("`r`n", "`n")
}
function Replace-Exact([string]$Text, [string]$Old, [string]$New, [string]$Label) {
    $first = $Text.IndexOf($Old, [System.StringComparison]::Ordinal)
    if ($first -lt 0) { Fail "Could not find expected code: $Label. Your branch may have changed. No files were modified." }
    if ($Text.IndexOf($Old, $first + 1, [System.StringComparison]::Ordinal) -ge 0) { Fail "Ambiguous code location: $Label. No files were modified." }
    return $Text.Substring(0, $first) + $New + $Text.Substring($first + $Old.Length)
}

try {
    $RepoPath = (Resolve-Path -LiteralPath $RepoPath).Path
    if (-not (Test-Path -LiteralPath (Join-Path $RepoPath 'gradlew') -PathType Leaf)) {
        Fail "This is not the RoundaboutMod project root. Move both fix files beside gradlew. Current folder: $RepoPath"
    }
    Write-Host "Project: $RepoPath" -ForegroundColor Cyan
    $base = 'common/src/main/java/'
    $powerPath = $base + 'com/example/jjf/stand/powers/PowersJumpinJackFlash.java'
    $modelPath = $base + 'com/example/jjf/client/models/stand/JumpinJackFlashModel.java'
    $screenPath = $base + 'net/hydra/jojomod/client/gui/PowerInventoryScreen.java'
    $jsonPath = 'common/src/main/resources/jjf.mixins.json'
    $remove = @(
        'common/src/main/java/com/example/jjf/mixin/JJFMobAIMixin.java'
        'common/src/main/java/com/example/jjf/mixin/JJFSpinAnimationMixin.java'
        'common/src/main/java/com/example/jjf/mixin/ScreenMixin.java'
        'common/src/main/java/com/example/jjf/mixin/AbstractContainerScreenAccessor.java'
    )

    $git = Get-Command git -ErrorAction SilentlyContinue
    if ($null -ne $git) {
        $branch = (& git -C $RepoPath branch --show-current 2>$null | Select-Object -First 1)
        if ($branch -and $branch -ne 'JumpinJackFlash') { Fail "You are on Git branch '$branch'. Switch to JumpinJackFlash in GitHub Desktop first." }
    }

    $updates = @{}
    $power = Read-Source $powerPath
    $power = Replace-Exact $power 'import net.minecraft.world.entity.LivingEntity;' "import net.minecraft.world.entity.LivingEntity;`nimport net.minecraft.world.entity.Mob;" 'Mob import'
    $oldText = @'
        super.tickPower();

        if (!jjf$spinAttackHeld) {
'@
    $newText = @'
        super.tickPower();
        jjf$tickMobAI();

        if (!jjf$spinAttackHeld) {
'@
    $power = Replace-Exact $power $oldText $newText 'JJF powers tick hook'
    $mobMethods = @'
    private void jjf$tickMobAI() {
        if (!(this.getSelf() instanceof Mob mob) || mob.level().isClientSide()) {
            return;
        }
        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }
        if (this.getActivePower() != PowerIndex.NONE) {
            return;
        }
        double distance = mob.distanceTo(target);
        if (distance < 6.0D || distance > 20.0D) {
            return;
        }
        if (target instanceof ZeroGravityAccess access && access.jjf$getZeroGravityState().isSpread()) {
            return;
        }
        if (this.onCooldown(PowerIndex.SKILL_2)) {
            return;
        }
        jjf$fireMobSpit(mob, target);
        this.setCooldown(PowerIndex.SKILL_2, 80);
    }

    private void jjf$fireMobSpit(Mob mob, LivingEntity target) {
        JJFSpitEntity spit = new JJFSpitEntity(mob.level(), mob);
        Vec3 start = new Vec3(mob.getX(), mob.getEyeY() - 0.12D, mob.getZ());
        Vec3 direction = new Vec3(target.getX(), target.getEyeY(), target.getZ())
                .subtract(start).normalize();
        start = start.add(direction.scale(0.35D));
        spit.setPos(start.x, start.y, start.z);
        spit.shoot(direction.x, direction.y, direction.z, 1.6F, 0.6F);
        mob.level().addFreshEntity(spit);
    }

'@
    $anchor = @'
    @Override
    public float inputSpeedModifiers(float basis) {
'@
    $power = Replace-Exact $power $anchor ($mobMethods + "`n" + $anchor) 'Insert JJF mob AI'
    $updates[$powerPath] = $power

    $model = Read-Source $modelPath
    $model = Replace-Exact $model 'import net.hydra.jojomod.entity.stand.FollowingStandEntity;' "import net.hydra.jojomod.entity.stand.FollowingStandEntity;`nimport net.hydra.jojomod.entity.stand.StandEntity;" 'StandEntity model import'
    $spinMethods = @'
    private void jjf$applySpinAnimation(T entity, float ageInTicks) {
        boolean spinning = entity.getAnimation() == StandEntity.BARRAGE_CHARGE;
        boolean releasing = entity.getAnimation() == StandEntity.BARRAGE_FINISHER;
        setCuffsSpinning(spinning || releasing);
        right_spin_on.yRot = 0.0F;
        left_spin_on.yRot = 0.0F;
        if (spinning) {
            float spin = ageInTicks * 1.6F;
            right_arm.xRot = -1.57F;
            left_arm.xRot = -1.57F;
            right_arm.yRot = 0.0F;
            left_arm.yRot = 0.0F;
            right_arm.zRot = 0.0F;
            left_arm.zRot = 0.0F;
            lower_right_arm.xRot = 0.0F;
            lower_left_arm.xRot = 0.0F;
            lower_right_arm.yRot = 0.0F;
            lower_left_arm.yRot = 0.0F;
            lower_right_arm.zRot = 0.0F;
            lower_left_arm.zRot = 0.0F;
            right_spin_on.yRot = spin;
            left_spin_on.yRot = -spin;
            upper_chest.xRot = 0.0F;
        }
        if (releasing) {
            right_arm.xRot = -1.45F;
            left_arm.xRot = -1.45F;
            right_arm.yRot = -0.22F;
            left_arm.yRot = 0.22F;
            lower_right_arm.xRot = 0.0F;
            lower_left_arm.xRot = 0.0F;
            lower_right_arm.yRot = 0.0F;
            lower_left_arm.yRot = 0.0F;
            upper_chest.xRot = 0.16F;
        }
    }

'@
    $anchor = @'
    @Override
    public void setupAnim(
'@
    $model = Replace-Exact $model $anchor ($spinMethods + "`n" + $anchor) 'Insert JJF model animation'
    $oldText = @'
        defaultAnimations(entity, ageInTicks, 1.0F);
'@
    $newText = @'
        defaultAnimations(entity, ageInTicks, 1.0F);
        jjf$applySpinAnimation(entity, ageInTicks);
'@
    $model = Replace-Exact $model $oldText $newText 'Call JJF model animation'
    $updates[$modelPath] = $model

    $screen = Read-Source $screenPath
    $screen = Replace-Exact $screen 'import com.google.common.collect.ImmutableList;' "import com.example.jjf.client.JJFHudSettings;`nimport com.example.jjf.stand.powers.PowersJumpinJackFlash;`nimport com.google.common.collect.ImmutableList;" 'Screen imports'
    $oldText = @'
        this.yMouse = (float)mouseY;
'@
    $newText = @'
        this.yMouse = (float)mouseY;
        jjf$renderHudSettings(context, mouseX, mouseY);
'@
    $screen = Replace-Exact $screen $oldText $newText 'Render HUD setting'
    $oldText = @'
    public boolean mouseClicked(double mouseX, double mouseY, int $$2) {
        Player pl = Minecraft.getInstance().player;
'@
    $newText = @'
    public boolean mouseClicked(double mouseX, double mouseY, int $$2) {
        if (jjf$handleHudSettingsClick(mouseX, mouseY, $$2)) {
            return true;
        }
        Player pl = Minecraft.getInstance().player;
'@
    $screen = Replace-Exact $screen $oldText $newText 'Click HUD setting'
    $screenMethods = @'
    private boolean jjf$showSettings() {
        Player player = Minecraft.getInstance().player;
        return player != null
                && isOptionsOut
                && pageNumber == 2
                && ConfigManager.getClientConfig().dynamicSettings.currentPowerInventoryTab == 2
                && ((StandUser) player).roundabout$getStandPowers() instanceof PowersJumpinJackFlash;
    }

    private void jjf$renderHudSettings(GuiGraphics context, int mouseX, int mouseY) {
        if (!jjf$showSettings()) {
            return;
        }
        int left = this.leftPos;
        int top = this.topPos;
        context.drawString(this.font, JJFHudSettings.ammoMessage(), left - 135,
                top + 125, isSurelyHovering(left - 136, top + 123, 118, 11, mouseX, mouseY)
                        ? 0xFFFFFF : 0xA0A0A0, false);
        context.drawString(this.font, JJFHudSettings.launcherMessage(), left - 135,
                top + 136, isSurelyHovering(left - 136, top + 134, 118, 11, mouseX, mouseY)
                        ? 0xFFFFFF : 0xA0A0A0, false);
    }

    private boolean jjf$handleHudSettingsClick(double mouseX, double mouseY, int button) {
        if (button != 0 || !jjf$showSettings()) {
            return false;
        }
        int left = this.leftPos;
        int top = this.topPos;
        if (isSurelyHovering(left - 136, top + 123, 118, 11, mouseX, mouseY)) {
            JJFHudSettings.toggleAmmoText();
        } else if (isSurelyHovering(left - 136, top + 134, 118, 11, mouseX, mouseY)) {
            JJFHudSettings.toggleLauncherText();
        } else {
            return false;
        }
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

'@
    $anchor = @'
    public void gearChange(){
'@
    $screen = Replace-Exact $screen $anchor ($screenMethods + "`n" + $anchor) 'Insert direct screen HUD controls'
    $updates[$screenPath] = $screen

    $mix = Read-Source $jsonPath | ConvertFrom-Json
    $removeNames = @('JJFMobAIMixin', 'JJFSpinAnimationMixin', 'ScreenMixin', 'AbstractContainerScreenAccessor')
    foreach ($name in $removeNames) {
        $all = @($mix.mixins) + @($mix.client)
        if ($all -notcontains $name) { Fail "Expected mixin '$name' not found in jjf.mixins.json. No files modified." }
    }
    $mix.mixins = @($mix.mixins | Where-Object { $removeNames -notcontains $_ })
    $mix.client = @($mix.client | Where-Object { $removeNames -notcontains $_ })
    $updates[$jsonPath] = (($mix | ConvertTo-Json -Depth 20) + "`n")
    foreach ($relative in $remove) {
        $null = Read-Source $relative
    }

    $stamp = Get-Date -Format 'yyyyMMdd_HHmmss'
    $backup = Join-Path $env:TEMP ('JJF_internal_mixin_backup_' + $stamp)
    $allpaths = @($updates.Keys) + $remove
    foreach ($relative in $allpaths) {
        $src = Join-Path $RepoPath $relative
        $dst = Join-Path $backup $relative
        [System.IO.Directory]::CreateDirectory([System.IO.Path]::GetDirectoryName($dst)) | Out-Null
        [System.IO.File]::Copy($src, $dst, $true)
    }
    foreach ($relative in $updates.Keys) {
        $dest = Join-Path $RepoPath $relative
        $old = [System.IO.File]::ReadAllText($dest)
        $newline = if ($old.Contains("`r`n")) { "`r`n" } else { "`n" }
        $content = $updates[$relative].Replace("`n", $newline)
        [System.IO.File]::WriteAllText($dest, $content, ([System.Text.UTF8Encoding]::new($false)))
        Write-Host "Updated: $relative" -ForegroundColor Green
    }
    foreach ($relative in $remove) {
        [System.IO.File]::Delete((Join-Path $RepoPath $relative))
        Write-Host "Removed obsolete mixin: $relative" -ForegroundColor Green
    }
    Write-Host "`nCompleted JJF internal-mixin refactor." -ForegroundColor Green
    Write-Host "Backup: $backup"
    Write-Host "Next: compile Fabric and Forge, test summon/spit/spin/HUD, then commit + push in GitHub Desktop."
} catch {
    Write-Host "`nSTOPPED: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Error location: $($_.InvocationInfo.ScriptName):$($_.InvocationInfo.ScriptLineNumber)" -ForegroundColor Yellow
    Write-Host "If the failure occurred before the backup step, no source files were changed."
    exit 1
}
