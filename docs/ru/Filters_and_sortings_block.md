---
title: 'Блоки фильтров и сортировок'
---

Блоки фильтров и сортировок [инструкции `FORM`](FORM_statement.md) - добавление [фильтров](Form_structure.md#filters) и [сортировок](Form_structure.md#sort) в структуру формы, а также [групп фильтров](Interactive_view.md#filtergroup) в интерактивное представление формы.

## Блок фиксированных фильтров {#fixedfilters}

### Синтаксис

    FILTERS expression1, ..., expressionN

### Описание

Блок фиксированных фильтров добавляет на форму фильтры, которые будут автоматически применяться при чтении любых данных на форме. В одном блоке можно перечислить через запятую произвольное количество фильтров.

Каждый фильтр задается [выражением](Expression.md), которое определяет условие фильтрации. Во всех выражениях в качестве параметров можно использовать имена уже объявленных объектов на форме.

### Параметры

- `expression1, ..., expressionN`

    Список выражений - фильтров.

### Примеры

```lsf
CLASS Stock;
name = DATA ISTRING[100] (Stock);
region = DATA Region (Stock);

CLASS Group;
name = DATA ISTRING[100] (Group);

group = DATA Group(Sku);
nameGroup (Sku s) = name(group(s));

active = DATA BOOLEAN (Sku);

onStock = DATA NUMERIC[10,2] (Stock, Sku);

FORM onStock 'Остатки' // создаем форму, в которой можно смотреть остатки по товарам
    OBJECTS r = Region PANEL // добавляем объект регион
    // добавляем свойство имя региона, при нажатии на который пользователь сможет его выбирать
    PROPERTIES name(r) SELECTOR 

    OBJECTS st = Stock // добавляем объект склады
    PROPERTIES name(st) READONLY // добавляем имя склада
    FILTERS region(st) == r // добавляем фильтр, чтобы показывались только склады выбранного региона

    OBJECTS s = Sku // добавляем товары
    // добавляем наименование группы товаров, присваивая ему groupName в качестве имени свойства на форме, 
    // а также наименование и остаток по товару
    PROPERTIES READONLY groupName = nameGroup(s), name(s), onStock(st, s) 
    FILTERS active(s) // включаем, чтобы показывались только активные товары
;
```


## Блок группы фильтров {#filtergroup}

### Синтаксис

    [EXTEND] FILTERGROUP groupName
        FILTER caption1 expression1 [keystroke1] [DEFAULT]
        ...
        FILTER captionN expressionN [keystrokeN] [DEFAULT]

### Описание

Блок группы фильтров добавляет на форму набор фильтров, для которых будет создан компонент в пользовательском интерфейсе, позволяющий применить в данный момент времени один из заданных фильтров. При указании ключевого слова `EXTEND` компонент не создается, а используется для расширения. В одном блоке можно определить только одну группу фильтров, состоящую из произвольного количества фильтров, причем пользователю они будут показываться в порядке их перечисления. 

Каждый фильтр задается [выражением](Expression.md), которое определяет условие фильтрации. Во всех выражениях в качестве параметров можно использовать имена уже объявленных объектов на форме.

### Параметры

<a className="lsdoc-anchor" id="filterName"/>

- `groupName`

    Внутреннее имя группы фильтров. [Простой идентификатор](IDs.md#id). При указании ключевого слова `EXTEND` будет осуществлен поиск на форме уже созданной группы фильтров с таким именем, иначе будет создана новая группа фильтров с указанным именем.

- `caption1, ..., captionN`

    Заголовки, которые будут показываться в пользовательском интерфейсе для соответствующего добавляемого фильтра. Каждый заголовок задается [строковым литералом](IDs.md#strliteral).

- `expression1, ..., expressionN`

    Выражения, описывающие фильтры.

- `keystroke1, ..., keystrokeN`

    Сочетания клавиш, при нажатии которого пользователем будет происходить выбор соответствующего фильтра в группе. Каждое сочетание клавиш задается строковым литералом, принцип задания аналогичен способу задания параметра в методе Java-класса [Keystroke.getKeystroke(String)](http://docs.oracle.com/javase/7/docs/api/javax/swing/KeyStroke.html#getKeyStroke(java.lang.String)).

- `DEFAULT`

    Ключевое слово, указывающее на то, что добавляемый фильтр должен быть выбран автоматически при открытии формы. Может быть задан только для одного фильтра в группе.
 

### Примеры

```lsf
active = DATA BOOLEAN (Stock);

EXTEND FORM onStock // расширяем ранее созданную форму с остатками
    // создаем группу фильтров с одним фильтрам, которая будет показываться в виде флажка, 
    // по которому пользователь сможет включать/отключать фильтр
    FILTERGROUP stockActive 
        // добавляем отбор по только активным складам, который будет применяться по нажатию клавиши F11
        FILTER 'Активные' active(st) 'F11' 
    // создаем новую группу фильтров, в которой пользователь сможет выбирать один из фильтров
    // при помощи выпадающего списка
    FILTERGROUP skuAvailability 
        // добавляем отбор, который будет показывать только товары на остатках, выбираться по клавише F10
        // и будет автоматически выбран при открытии формы
        FILTER 'Есть на остатках' onStock(st, s) 'F10' DEFAULT 
;

// ...

EXTEND FORM onStock
    EXTEND FILTERGROUP skuAvailability
        FILTER 'Отрицательные остатки' onStock(st, s) < 0 'F9' // добавляем отбор по выражению
;
```


## Блок сортировок {#sort}

### Синтаксис

    ORDER formPropertyName1 [DESC] 
          ...
          formPropertyNameN [DESC]

### Описание

Блок сортировок добавляет на форму сортировки, которые будут автоматически применяться при чтении любых данных на форме. В одном блоке можно перечислить через запятую любое количество свойств на форме в произвольной последовательности. Эти свойства должны быть предварительно добавлены на форму.

### Параметры

- `formPropertyName1, ..., formPropertyNameN`

    Имена свойств или действий на форме, по которым необходимо осуществлять сортировку.

- `DESC`

    Ключевое слово. Указывает на обратный порядок сортировки. По умолчанию используется сортировка по возрастанию.

### Примеры

```lsf
EXTEND FORM onStock // расширяем ранее созданную форму с остатками
    ORDERS name(s) // включаем сортировку по наименования склада в списке складов
    // Включаем сортировку по возрастанию имени группы, а внутри по убыванию остатка на складе.
    // Следует отметить, что в качестве свойства указывается имя свойства на форме groupName,
    // а не просто имя свойства nameGroupSku
    ORDERS groupName, onStock(st, s) DESC 
;
```