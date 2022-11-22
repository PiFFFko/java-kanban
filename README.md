# Трекер задач
------
## Техническое задание
Как человек обычно делает покупки? Если ему нужен не один продукт, а несколько, то очень вероятно, что сначала он составит список, чтобы ничего не забыть. Сделать это можно где угодно: на листе бумаги, в приложении для заметок или, например, в сообщении самому себе в мессенджере.
А теперь представьте, что это список не продуктов, а полноценных дел. И не каких-нибудь простых вроде «помыть посуду» или «позвонить бабушке», а сложных — например, «организовать большой семейный праздник» или «купить квартиру». Каждая из таких задач может разбиваться на несколько этапов со своими нюансами и сроками. А если над их выполнением будет работать не один человек, а целая команда, то организация процесса станет ещё сложнее. Для этого существуют так называемые "Трекеры задач".

Данное приложения является бэкендом для такого трекера.
Программа должна отвечать за формирование модели данных для страницы изображенной ниже:

![](https://pictures.s3.yandex.net/resources/Untitled_25_1639469823.png)
------


### Типы задач

Простейшим кирпичиком такой системы является **задача** (англ. task). У задачи есть следующие свойства:
1. Название, кратко описывающее суть задачи (например, «Переезд»).
2. Описание, в котором раскрываются детали.
3. Уникальный идентификационный номер задачи, по которому её можно будет найти.
4. Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
* NEW — задача только создана, но к её выполнению ещё не приступили.
* IN_PROGRESS — над задачей ведётся работа.
* DONE — задача выполнена. 

Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask). Большую задачу, которая делится на подзадачи, мы будем называть эпиком (англ. epic).
Таким образом, в нашей системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. Для них должны выполняться следующие условия:
* Для каждой подзадачи известно, в рамках какого эпика она выполняется.
* Каждый эпик знает, какие подзадачи в него входят.
* Завершение всех подзадач эпика считается завершением эпика.

------

### Идентификатор задачи

У каждого типа задач есть идентификатор. Это целое число, уникальное для всех типов задач. По нему мы находим, обновляем, удаляем задачи. При создании задачи менеджер присваивает ей новый идентификатор.

------

### Менеджер
Кроме классов для описания задач, вам нужно реализовать класс для объекта-менеджера. Он будет запускаться на старте программы и управлять всеми задачами. В нём должны быть реализованы следующие функции:
1. Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
2. Методы для каждого из типа задач(Задача/Эпик/Подзадача):
* Получение списка всех задач.
* Удаление всех задач.
* Получение по идентификатору.
* Создание. Сам объект должен передаваться в качестве параметра.
* Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
* Удаление по идентификатору.
3. Дополнительные методы:
* Получение списка всех подзадач определённого эпика.
4. Управление статусами осуществляется по следующему правилу:
* Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
5. Для эпиков:
* если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
* если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
* во всех остальных случаях статус должен быть IN_PROGRESS.


------
Разработка ведется студентом курса по Java разработке на сервисе Яндекс Практикум Пивнем Романом. \m/
