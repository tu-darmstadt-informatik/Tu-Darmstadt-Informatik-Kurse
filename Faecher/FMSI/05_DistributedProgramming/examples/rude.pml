mtype = { nice, rude };
chan request = [0] of { mtype, chan };


active [2] proctype Server() {
  mtype msg; chan reply;
end:
  do
    :: request ? msg, reply ->
       reply ! msg
  od
}

active proctype NiceClient() {
  mtype msg;
  chan reply = [0] of { mtype };
  request ! nice, reply;
  reply ? msg;
  assert (msg == nice)
}

active proctype RudeClient() {
  mtype msg;
  chan reply = [0] of { mtype };
  request ! rude, reply;
  reply ? msg
}
