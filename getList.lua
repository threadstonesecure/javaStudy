local value = redis.call('lindex', KEYS[1], ARGV[1])
local vType = type(value)
return value
