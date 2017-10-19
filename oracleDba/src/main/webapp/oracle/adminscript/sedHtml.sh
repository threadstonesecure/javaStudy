sed -i ".bak" '3a\
   <script type="text/javascript" async src="https://dn-lbstatics.qbox.me/busuanzi/2.3/busuanzi.pure.mini.js" ></script>\
' $1

sed  -i ".bak" "s/'is-mac'>/'is-mac'>阅读:<span id="busuanzi_value_page_pv"><\/span>/1" $1

rm $1.bak