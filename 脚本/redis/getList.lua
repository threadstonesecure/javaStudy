local v = redis.call('lindex', KEYS[1], ARGV[1])
local vType = type(v)
local randomId, value = struct.unpack('dLc0', v)
vType = type(randomId)
vType = type(value)
return value
