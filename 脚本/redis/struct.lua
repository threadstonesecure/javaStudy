-- struct.pack('dLc0', string.len(key), key, string.len(val), val);
-- local v = struct.pack('dLc0', ARGV[1], string.len(ARGV[2]), ARGV[2]);  -- 数值 和 长度字符串
-- local randomId,value = struct.unpack('dLc0', v);
local v = struct.pack('Lc0Lc0', string.len(ARGV[1]),ARGV[1], string.len(ARGV[2]), ARGV[2]); -- 两个长度字符串
local value1,value2= struct.unpack('Lc0Lc0', v);
return value1 .. value2;
