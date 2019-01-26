mtype = { nice, rude };
chan request = [0] of { mtype };
chan reply = [0] of { mtype };

active proctype Server() {
  mtype msg;
end:
  do
    :: request ? msg -> reply ! msg
  od
}

active proctype NiceClient() {
  mtype msg;
  request ! nice;
  reply ? msg;
  assert (msg == nice)
}

active proctype RudeClient() {
  mtype msg;
  request ! rude;
  reply ? msg
}
