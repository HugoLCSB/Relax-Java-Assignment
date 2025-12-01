# Game Server API

This server provides three main endpoints to interact with a game simulation. Below are the endpoints, along with the HTTP methods and query parameters needed for testing.

## Endpoints

### 1. Spin Endpoint

**URL**: `/spin`

**Method**: `POST`

This endpoint executes a spin in the game and returns the result.

**Parameters**:
- [optional]`seed` (long): The seed for random number generation.
- `bet` (double): The amount of money to bet.

**Example**:
```bash
curl -X POST "http://localhost:8080/spin?seed=0&bet=10"
```

**Response**:
```bash
{
  "seed" : 0,
  "bet" : 10.0,
  "steps" : {
    "0" : {
      "grid" : [ [ "H4", "H2", "H4", "H3", "H1", "H1", "L7", "H2" ], [ "H4", "L7", "BL", "L7", "H2", "L5", "L8", "H3" ], [ "H2", "L7", "L5", "L5", "H2", "BL", "BL", "BL" ], [ "L5", "BL", "H4", "L6", "L7", "L8", "L6", "H1" ], [ "L7", "L7", "H4", "L7", "L7", "BL", "H3", "BL" ], [ "L5", "H4", "BL", "H3", "L7", "H1", "L7", "H4" ], [ "H2", "L6", "BL", "L6", "L6", "L6", "L8", "WR" ], [ "L7", "WR", "L8", "H1", "H1", "H4", "WR", "H1" ] ],
      "clusters" : [ ],
      "gridAfterDestroy" : null,
      "gridAfterGravity" : null,
      "stepPayout" : 0.0
    }
  },
  "totalPayout" : 0.0
}
```
---

### 2. Gamble Endpoint

**URL**: `/gamble`

**Method**: `POST`

This endpoint executes a 50/50 gamble for the player at the end of the turn where he has a choice of gambling for double or nothing.

**Parameters**:
- [optional]`seed` (long): The seed for random number generation.
- `bet` (double): The amount of money to bet.

**Example**:
```bash
curl -X POST "http://localhost:8080/gamble?seed=0&bet=10"
```
**Response**:
```bash
{
  "seed" : -6322723253012796480,
  "bet" : 10.0,
  "steps" : null,
  "totalPayout" : 0.0
}
```
---

### 3. Simulation Endpoint

**URL**: `/sim`

**Method**: `POST`

This endpoint executes a simulation, doing n spins and summing the payouts to calculate the RTP (return to player).

**Parameters**:
- `spins` (int): The number of spins to simulate.
- [optional]`batchSize` (int): The batch size to use for the simulation.

**Example**:
```bash
curl -X POST "http://localhost:8080/sim?spins=100000&batchSize=10000"
```
**Response**:
```bash
{
  "spins" : 100000,
  "rtp" : 0.5901330000000033,
  "seconds" : 4.0
}
```
---
