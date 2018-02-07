local oldV = redis.call('del','name');
redis.call('set','name','xxxx','sdfsdf');
return oldV;
