local value = redis.call('hget', KEYS[1], ARGV[1])
local vType = type(value)
return value
