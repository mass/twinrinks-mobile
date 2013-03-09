#import "VC_ScheduleGames.h"
#import "Game.h"
#import "MemoryManager.h"

@interface VC_ScheduleGames ()

@property (nonatomic,strong) __block NSArray *gameArray;

@end

@implementation VC_ScheduleGames

@synthesize dataToDisplay;

-(id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {}
    return self;
}

-(void)viewDidLoad {
    [super viewDidLoad];
    
    MemoryManager *myManager = [[MemoryManager alloc] init];
    _gameArray = [myManager getGameArray];
    
    if([dataToDisplay isEqualToString:@"AllGames"])
        _gameArray = [_gameArray sortedArrayUsingComparator:^NSComparisonResult(Game *a, Game *b) {
            return [[a getDateObject] compare:[b getDateObject]];
        }];
    else if([dataToDisplay isEqualToString:@"Today"])
        [self trimGameArrayForToday];
    else if([dataToDisplay isEqualToString:@"Playoffs"])
        [self trimGameArrayForPlayoffs];
    else {
        NSArray *data = [dataToDisplay componentsSeparatedByString:@","];
        [self trimGameArrayForTeam:[((NSString *) [data objectAtIndex:1]) stringByReplacingOccurrencesOfString:@";" withString:@""] andLeague:[data objectAtIndex:0]];
    }
    
    self.tableView.backgroundView = nil;
    self.view.backgroundColor = [UIColor colorWithRed:0.2f green:0.2f blue:0.2f alpha:1.0f];
    [self.tableView reloadData];
}

-(void)trimGameArrayForTeam:nameP andLeague:leagueP {
    NSMutableArray *tempArray = [NSMutableArray arrayWithArray:_gameArray];
    
    for(int i=tempArray.count-1;i>=0;i--) {
        Game *temp = [tempArray objectAtIndex:i];
        if((![temp.league isEqualToString:leagueP]) || (!([temp.homeTeam isEqualToString:nameP] || [temp.awayTeam isEqualToString:nameP])))
            [tempArray removeObjectAtIndex:i];
    }
    
    _gameArray = [tempArray sortedArrayUsingComparator:^NSComparisonResult(Game *a, Game *b) {
        return [[a getDateObject] compare:[b getDateObject]];
    }];
}

-(void)trimGameArrayForToday {
    NSMutableArray *tempArray = [NSMutableArray arrayWithArray:_gameArray];
    
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"MM/dd/yy"];
    NSString *todayString = [format stringFromDate:[NSDate date]];
    
    for(int i=tempArray.count-1;i>=0;i--)
        if(![((NSString *)(((Game *)[tempArray objectAtIndex:i]).date))isEqualToString:todayString])
            [tempArray removeObjectAtIndex:i];
    _gameArray = [tempArray sortedArrayUsingComparator:^NSComparisonResult(Game *a, Game *b) {
        return [[a getDateObject] compare:[b getDateObject]];
    }];
}

-(void)trimGameArrayForPlayoffs {
    NSMutableArray *tempArray = [NSMutableArray arrayWithArray:_gameArray];
    
    for(int i=tempArray.count-1;i>=0;i--)
        if(![((Game *)[tempArray objectAtIndex:i]).homeTeam isEqualToString:@"PLAYOFFS"])
            [tempArray removeObjectAtIndex:i];
    _gameArray = [tempArray sortedArrayUsingComparator:^NSComparisonResult(Game *a, Game *b) {
        return [[a getDateObject] compare:[b getDateObject]];
    }];
}

#pragma mark - Table view data source

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _gameArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"id_cell_ScheduleGames";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil)
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    
    UILabel *homeLabel = (UILabel *)[cell viewWithTag:1];
    UILabel *awayLabel = (UILabel *)[cell viewWithTag:2];
    UILabel *dayDateTimeLabel = (UILabel *)[cell viewWithTag:3];
    UILabel *leagueLabel = (UILabel *)[cell viewWithTag:4];
    UILabel *rinkLabel = (UILabel *)[cell viewWithTag:5];
    
    Game *temp = ((Game *)[_gameArray objectAtIndex:indexPath.row]);
    
    homeLabel.text =  temp.homeTeam;
    awayLabel.text = temp.awayTeam;
    leagueLabel.text = temp.league;
    rinkLabel.text = temp.rink;
    dayDateTimeLabel.text = [NSString stringWithFormat:@"%@ on %@, at %@",temp.day,temp.date,temp.startTime];
    
    return cell;
}

@end