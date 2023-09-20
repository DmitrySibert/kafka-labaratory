### Partition test

**profile**: partition

**purpose**: demonstrate how produced events are distributed between two partitions and then two subscriber-applications consume events.
Each one from its own partition

**note**: events are distributed between partitions only when there a lot of events are published with quite good speed. Some balance logic is baked within producer itself.

---

### Consumer group test

**profile**: consumer-group

**purpose**: demonstrate that two consumers from different group receive all events

---

### Replication test

**profile**: replication

**purpose**: see that subscriber consumes events from replica node once master is lost

**note**: doesn't work with this demonstration project, because I didn't find the way to change replicas acknowledge number

---

### Load test

**profile**: highload

**purpose**: see performance benefits from using two nodes versus one node


