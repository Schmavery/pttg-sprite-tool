pttg-sprite-tool
================

Sprite editor tool for the procedural-time-travel-game.

This is a tool for creating metadata to go with spritesheets when loading them into the game in order to define useful information about the sprites.

Finished Features:
-----------------

- Split spritesheets into multiple images for separate editing.
- Set anchor point to define the center of the image.
- Hooks: Labelled points on the image to which behaviours can be attached.
- Bounding Boxes: Draw a convex polygon to define a sprite's collision box.
  - Ability to automatically generate a convex hull for a sprite.
- Animation support
  - Edit animation speed
  - Choose frames
  - Preview animations

Future Features:
----------------
- Palette areas: Define areas that should be the same color, so that palette switching can be done later.
- Basic sprite editing
  - Pencil tool
  - Color picker
  - Eye dropper
  - Save changes, export to original spritesheet.
