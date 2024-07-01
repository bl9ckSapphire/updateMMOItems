# 메인핸드의 MMOItems 아이템을 수동으로 업데이트합니다

**[ 제작 의도 ]**
```
MMOItems 의 RevisionID 기능으로 아이템을 업데이트 시키는 기능이 존재하지만,

실시간으로 바로바로 변경사항을 업데이트 할 수 있는 기능은 존재하지 않아 보여서 직접 제작했습니다.
```

**[ 기능 ]**
```
메인핸드에 들고있는 MMOItems 를 가장 최근의 데이터로 수정합니다.

MMOItems 가 아닌 아이템, 아무것도 들고있지 않을 때, 이미 최신화된 아이템은 수행되지 않습니다.

인벤토리에 공간을 감지하여, 공간이 없을경우 수행되지 않습니다. 
```

**[ 종속성 ]**
```
해당 플러그인은 NBT-API 를 필요로 합니다.

NBT-API 최신 빌드 >> https://modrinth.com/plugin/nbtapi/version/2.13.1
```

**[ 명령어 ]**
```
"/djqepdlxm(업데이트) , /업데이트" 2개의 aliases 가 존재합니다.

1. [ /djqepdlxm(업데이트) ] >> 메인핸드에 들고있는 MMOItems 업데이트를 실행합니다.

2. [ /djqepdlxm(업데이트) reload ] >> 플러그인의 config 를 리로드합니다.
```

**[ 펄미션 (Permission) ]**
```
1. updatemmoitems.use = 업데이트 명령어 사용 펄미션 (기본적으로 모든 유저가 사용가능하도록 설계했습니다.)

2. updatemmoitems.reload = 콘피그파일 리로드 펄미션 (기본적으로 OP만 사용가능하도록 설계했습니다.)
```
