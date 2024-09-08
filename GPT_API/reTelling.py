import g4f
from requests import get


def translateEn(text, target_lang="en"):
    url = f'https://translate.googleapis.com/translate_a/single?client=gtx&sl=ru&tl={target_lang}&dt=t&q={text}'
    response = get(url)
    return response.json()[0][0][0]


def translateRu(text, target_lang="ru"):
    url = f'https://translate.googleapis.com/translate_a/single?client=gtx&sl=zh&tl={target_lang}&dt=t&q={text}'
    response = get(url)
    return response.json()[0][0][0]


def reTelling(text):
    text = "Перескажи текст развёрнуто в несколько предложений: " + text
    prompt = translateEn(text)

    response = g4f.ChatCompletion.create(
        model="gpt-3.5-turbo",
        messages=[{"role": 'user', 'content': prompt}],
        stream=True)

    final_message = ""
    for message in response:
        final_message += message
    final_message = translateRu(final_message)

    return final_message


def split_text(text, chunk_size=1000):
    return [text[i:i+chunk_size] for i in range(0, len(text), chunk_size)]


def process_large_text(text, chunk_size=1000):
    chunks = split_text(text, chunk_size)
    processed_chunks = [reTelling(chunk) for chunk in chunks]
    return ' '.join(processed_chunks)