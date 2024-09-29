import json
import os
import sys

gojyuon = [
    'あ', 'い', 'う', 'え', 'お',
    'か', 'き', 'く', 'け', 'こ',
    'さ', 'し', 'す', 'せ', 'そ',
    'た', 'ち', 'つ', 'て', 'と',
    'な', 'に', 'ぬ', 'ね', 'の',
    'は', 'ひ', 'ふ', 'へ', 'ほ',
    'ま', 'み', 'む', 'め', 'も',
    'や', 'ゆ', 'よ',
    'ら', 'り', 'る', 'れ', 'ろ',
    'わ', 'を', 'ん'
]

def register(hira, kanji):
    dictionary = {}
    path = 'src/main/resources/gomenne.json'

    with open(path, 'r', encoding='utf-8') as f:
        dictionary = json.load(f)

    dictionary[hira] = kanji
    sorted_dictionary = dict(sorted(dictionary.items(), key=lambda x: x[0]))

    with open(path, 'w', encoding='utf-8') as f:
        json.dump(sorted_dictionary, f, ensure_ascii=False, indent=2)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Correct syntax: python mkdic.py <hira> <kanji>')
    else:
        arg1 = sys.argv[1]
        arg2 = sys.argv[2]
        register(arg1, arg2)
        print('Registered in the dictionary.')