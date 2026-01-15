## v2.2 (1.21)
- large scale internal rework
  - now it makes usage of module stats for easier addon and adjustments
  - other armor modules now extend default armor modules
  - if you want to work on armor we recommend checking armory and its updated way of doing armor modules.
- changed all armors durability calculation to closer fit vanilla (thanks @pixale) -> materials can now set armor_durability directly, they are multiplied for each module.
- changed how armor toughness is calculated -> materials can now define armor_toughness to overwrite it
- changed heavy armor, scuba and wing calculation for armor points, they now are a flat percentage better or worse then normal armor
- added gemstone armor (thanks to pixale)