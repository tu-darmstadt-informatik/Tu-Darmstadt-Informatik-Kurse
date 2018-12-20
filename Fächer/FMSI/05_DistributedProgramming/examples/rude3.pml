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
  chan reply = [0] of { mtype };
  mtype msg;
  request ! nice, reply;
  reply ? msg;
  assert( msg == nice )
}

active proctype RudeClient() {
  chan reply = [0] of { mtype };
  mtype msg;
  request ! rude, reply;
  reply ? msg
}

