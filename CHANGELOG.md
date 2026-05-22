Glassential Renewed 3.4.3
================
### Added
- Glass Painter Brush now works as an eyedropper. Shift + right-click any
  block to copy its color into the brush — works on Colored Glass (exact
  RGB), any block with a color tint like grass/leaves/water (vanilla and
  modded), and falls back to the block's map color for everything else.
- Colorable Glass Pane and Colorable Stained Glass Pane — pane versions
  of the colorable glass blocks. Full RGB picker, light emission,
  redstone signal and pass-through flags all work the same. Paint them
  with the Glass Painter Brush, copy colors with the eyedropper. Craft
  16 panes from 6 colorable glass like a vanilla pane recipe.
### Changed
- Colorable Glass and Colorable Stained Glass now show a rainbow 3D
  cube in the inventory, so they're easy to spot. (The placed block
  is still a clean white base that paints to any RGB color.)
- Clear Fluid Glass and Clear Fluid Fake Glass got distinct
  water-themed inventory icons.
- Colorable Glass Pane and Colorable Stained Glass Pane use matching
  rainbow icons.

### Fixed
- One Way Glass tooltip no longer shows a literal "\n" — the
  description now wraps onto a second line properly.

Glassential Renewed 3.4.2
================
- Added Tinted One Way Glass
- Fix: cull water faces adjacent to Clear Fluid Glass via LiquidBlockRenderer mixin
- Updated PT_BR localization (thanks PrincessStellar)
- One Way Glass faces now connect via Fusion CTM, just like the rest
  of the glass blocks. Works on both sides of the block, including
  through the opaque (mimic) face when the mimic is glass.

Glassential Renewed 3.4.1
================
- Color picker fixes [#49](https://github.com/bigenergy/glassential-renewed/issues/49)

Glassential Renewed 3.4.0
================
### New
- **Glassential Brush** - a new tool for customizing colored glass
- Opens a GUI with an HSV color picker for selecting any color
- 4 configurable properties:
- **Emit Light** - glass emits light (level 15)
- **Emit Redstone Signal** - glass emits a redstone signal (strength 15)
- **Pass Player** - player passes through glass
- **Pass All Entities** - all entities pass through glass
- RMB in the air - open settings
- RMB on colored glass - apply saved settings
- - **Colorable Glass** - programmable transparent glass
- Any RGB color via brush
- CTM (fusion) support
- Dynamic properties of light, redstone, and collision
- - **Colorable Stained Glass** - Programmable Colorable Glass
- All the features of standard Colorable Glass
- Tinted version for better visual effects

Glassential Renewed 3.3.3
================
- Fixed breaking slab block gives only one slab
- Mobs can no longer spawn on glass without collision
- Misc fixes

Glassential Renewed 3.3.2
================
- Updated zh_cn translate (thanks Ironnoob73)
- Fixed Clear Fluid Fake Glass damage in survival mode
- Fixed Tinted Ethereal Glass Pane & Tinted Reverse Ethereal Glass - now tinted
- Fixed Ghost Glass and Gravity Glass placing in water

Glassential Renewed 3.3.1
================
- Hotfix: missing register tinted glass trapdoor
- Nearly perfect Glass Slab connection (thanks Ironnoob73)

Glassential Renewed 3.3.0
================
- No more support fabric, only neoforge
- Rewrite code
- Added tags for all mod blocks
- Added One Way Glass (secret window)
- Functional glasses moved to creative sub tab
- Added Clear Fluid Glass (no waterfog glass) + fake variant (collide player)
- Added wood-type glass doors/trapdoors

Glassential Renewed 3.2.5
================
- Added gravity glass
- Hotfix loot drops

Glassential Renewed 3.2.4
================
- Updated zh_cn translate (thanks Ironnoob73)
- Fixed tinted redstone/light/lamp glass so it doesn't let light through anymore
- Changed lava_flow -> lava_still for lava lamps
- Fixed glass slab side blur
- Tinted door/trapdoor now really tinted (fix)
- Tinted light/redstone pane now really tinted (fix)
- Fixed silk touch drops all blocks
- Ghost door/trapdoor now has properties of its glass

Glassential Renewed 3.2.3
================
- Updated zh_cn translate (thanks Ironnoob73)
- Updated pt_br translate (thanks PrincessStelllar)
- Added ja_jp translate (thanks Abbage230)

Glassential Renewed 3.2.2
================
- Fixed wrong recipe (fix [#34](https://github.com/bigenergy/glassential-renewed/issues/34))

Glassential Renewed 3.2.1
================
- Added lava lamp & tinted
- Added glowstone lamp & tinted
- Redstone glass door & trapdoor now emits redstone signal
- Ethereal (Trap)doors now follow collision rules of block forms (fix [#24](https://github.com/bigenergy/glassential-renewed/issues/24))
- Added iron glass

Glassential Renewed 3.2.0
================
- Added tinted redstone glass & pane
- Added tinted light glass & pane
- Added ice glass
- Changed Tinted Glass Door item to Vanilla-look
- Changed Tinted Glass Door & Trapdoor to Vanilla-look
- Fixed redstone door model
- Some code refactoring
