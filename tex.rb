# LaTeX形式でプログラムリスト出力するスクリプト

filename = "test.tex"
File.open(filename, mode = "w") do |f|
  Dir.glob('**/*.java') do |src|
    p src
    caption = src.scan(/(.*)\.java/)[0][0].gsub(/[\/\\]/, '.') # 正規表現で\を.に置換
    label = src.scan(/([^\/\\]*?)\.java/)[0][0] # 正規表現でクラス名を抜き出す
    path = "" + src
    f.puts "\\lstinputlisting[caption=#{caption},label=#{label}]{#{path}}"
  end
end

# \lstset{%
#     language={C},
#     basicstyle={\small\ttfamily\footnotesize},%
#     identifierstyle={\small},%
#     commentstyle={\small\itshape},%
#     keywordstyle={\small\bfseries},%
#     ndkeywordstyle={\small},%
#     stringstyle={\small\ttfamily},
#     frame={tb},
#     breaklines=true,
#     columns=[l]{fullflexible},%
#     numbers=left,%
#     xrightmargin=0zw,%
#     xleftmargin=3zw,%
#     numberstyle={\scriptsize},%
#     stepnumber=1,
#     numbersep=1zw,%
#     lineskip=-0.5ex%
# }