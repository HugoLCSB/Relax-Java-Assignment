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
