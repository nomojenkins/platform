---
title: 'Блок настройки сводных таблиц'
---

Блок настроек сводных таблиц [инструкции `FORM`](FORM_statement.md) - управление начальными настройками [видов отображения *сводная таблица*](Interactive_view.md#property) в интерактивном представлении формы.

### Синтаксис

```
PIVOT 
    pivotSettingsBlock1
    ... 
    pivotSettingsBlockN
```

Где каждый `pivotSettingsBlocki` является блоком настроек. Эти блоки могут быть перечислены в произвольном порядке. Каждый блок может иметь один из следующих синтаксисов:

```
COLUMNS colFormPropertyList1, ..., colFormPropertyListM 
ROWS rowFormPropertyList1, ..., rowFormPropertyListK 
MEASURES measureFormProperty1, ..., measureFormPropertyL
groupObjectId pivotOptions 
```

Каждый из `colFormPropertyListi` и `rowFormPropertyListj` может описывать либо одиночное свойство на форме, либо группу свойств на форме:

```
formPropertyName
(formPropertyName1, ..., formPropertyNameX)
```

Опции `pivotOptions` могут быть перечислены друг за другом в произвольном порядке. Поддерживается следующий набор опций:

```
pivotType
calcType
settingsType
```

### Описание

Блок `PIVOT` позволяет задать начальные настройки сводных таблиц формы. С его помощью можно добавить свойства на форме в соответствующие списки колонок (блок `COLUMNS`), рядов (блок `ROWS`) и измерений (блок `MEASURES`) сводной таблицы, а также указать начальные значения некоторых опций сводных таблиц.

### Параметры 

- `formPropertyName`

    [Имя свойства на форме](Properties_and_actions_block.md#name). 

- `measureFormProperty1, ..., measureFormPropertyL`

    Список имен свойств на форме. Определяет свойства на форме, которые добавляются в списки измерений соответствующих сводных таблиц.

- `groupObjectId`

    [Идентификатор группы объектов](IDs.md#groupobjectid), к которой применяются опции из описываемого блока настроек.

- `pivotType`

    [Строковый литерал](Literals.md#strliteral), который определяет начальный вид отображения сводной таблицы. Может принимать одно из следующих значений:
    
    - `'Table'` (значение по умолчанию)
    - `'Table Bar Chart'`
    - `'Table Heatmap'`
    - `'Table Row Heatmap'`
    - `'Table Col Heatmap'`
    - `'Bar Chart'`
    - `'Stacked Bar Chart'`
    - `'Line Chart'`
    - `'Area Chart'`
    - `'Scatter Chart'`
    - `'Multiple Pie Chart'`
    - `'Horizontal Bar Chart'`
    - `'Horizontal Stacked Bar Chart'`
  
- `calcType`
       
    Указание начальной агрегирующей функции. Может задаваться одним из ключевых слов:

    - `SUM` - сумма значений (значение по умолчанию)
    - `MAX` - максимум значений
    - `MIN` - минимум значений
         
- `settingsType`

    Указание того, показываются ли пользователю настройки сводной таблицы. Может задаваться одним из ключевых слов:
    
    - `SETTINGS` - настройки показываются (значение по умолчанию)
    - `NOSETTINGS` - настройки не показываются
