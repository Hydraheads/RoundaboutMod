# Chocolate Disco — Roundabout integration

This source tree integrates Chocolate Disco directly into Roundabout. There is no separate the old helper mod/package package, mod id, entrypoint, resource namespace, or networking implementation left in the project.

## Networking

Chocolate Disco client/server traffic now uses Roundabout's existing `ModMessageEvents` / `ClientToServerPackets` / `ServerToClientPackets` system. The old Fabric `ClientPlayNetworking` / `ServerPlayNetworking` implementation was removed.

The Chocolate Disco C2S messages are handled by `ClientToServerPackets.StandPowerPackets`, and the S2C state/sound messages are handled by `ServerToClientPackets.S2CPackets` and `ClientUtil`.

## Platform separation

Chocolate Disco gameplay, networking, state, mixins, models, renderers, screens, sounds, and resources are in the common source/resources where possible. Fabric and Forge only provide their platform-specific registration/event hooks.

- Fabric: `RoundaboutChocolateDiscoFabricClient`
- Forge: `ChocolateDiscoForgeClientEvents`
- Shared mixins: `roundabout.chocolatedisco.mixins.json`

The three previous obfuscation-mapping problems involving `onStandSwitch`, `powerActivate`, and `isAttackIneptVisually` are addressed by marking injections into Roundabout-owned classes as `remap = false`.

## Resources

Chocolate Disco assets are under the Roundabout namespace (`assets/roundabout`) and the Chocolate Disco sounds are registered through Roundabout's platform sound registries.
